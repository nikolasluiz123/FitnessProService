package br.com.fitnesspro.scheduler.service

import br.com.fitnesspro.authentication.repository.auditable.IPersonRepository
import br.com.fitnesspro.authentication.service.DeviceService
import br.com.fitnesspro.common.repository.jpa.general.academy.ICustomAcademyRepository
import br.com.fitnesspro.common.service.firebase.FirebaseNotificationService
import br.com.fitnesspro.core.cache.SCHEDULER_IMPORT_CACHE_NAME
import br.com.fitnesspro.core.enums.EnumDateTimePatterns
import br.com.fitnesspro.core.exceptions.BusinessException
import br.com.fitnesspro.core.extensions.dateNow
import br.com.fitnesspro.core.extensions.format
import br.com.fitnesspro.core.extensions.timeNow
import br.com.fitnesspro.core.gson.defaultGSon
import br.com.fitnesspro.models.general.Person
import br.com.fitnesspro.models.scheduler.Scheduler
import br.com.fitnesspro.scheduler.repository.auditable.ISchedulerRepository
import br.com.fitnesspro.scheduler.repository.jpa.ICustomSchedulerRepository
import br.com.fitnesspro.scheduler.service.mappers.SchedulerServiceMapper
import br.com.fitnesspro.service.communication.dtos.scheduler.ValidatedRecurrentSchedulerDTO
import br.com.fitnesspro.service.communication.dtos.scheduler.ValidatedSchedulerDTO
import br.com.fitnesspro.shared.communication.dtos.scheduler.interfaces.ISchedulerDTO
import br.com.fitnesspro.shared.communication.enums.notification.EnumNotificationChannel
import br.com.fitnesspro.shared.communication.enums.scheduler.EnumSchedulerSituation
import br.com.fitnesspro.shared.communication.enums.scheduler.EnumSchedulerType
import br.com.fitnesspro.shared.communication.notification.SchedulerNotificationCustomData
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.CommonImportFilter
import br.com.fitnesspro.workout.repository.auditable.IWorkoutGroupRepository
import br.com.fitnesspro.workout.repository.auditable.IWorkoutRepository
import br.com.fitnesspro.workout.service.mappers.WorkoutServiceMapper
import com.google.gson.GsonBuilder
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.context.MessageSource
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Service
class SchedulerService(
    private val schedulerRepository: ISchedulerRepository,
    private val customSchedulerRepository: ICustomSchedulerRepository,
    private val personRepository: IPersonRepository,
    private val customAcademyRepository: ICustomAcademyRepository,
    private val workoutRepository: IWorkoutRepository,
    private val workoutGroupRepository: IWorkoutGroupRepository,
    private val firebaseNotificationService: FirebaseNotificationService,
    private val deviceService: DeviceService,
    private val schedulerServiceMapper: SchedulerServiceMapper,
    private val workoutServiceMapper: WorkoutServiceMapper,
    private val messageSource: MessageSource
) {
    @CacheEvict(cacheNames = [SCHEDULER_IMPORT_CACHE_NAME], allEntries = true)
    fun saveScheduler(schedulerDTO: ValidatedSchedulerDTO) {
        validateScheduler(schedulerDTO)

        when (schedulerDTO.type!!) {
            EnumSchedulerType.SUGGESTION, EnumSchedulerType.UNIQUE -> {
                val oldSchedulerDTO = schedulerRepository.findById(schedulerDTO.id!!).getOrNull()?.let {
                    schedulerServiceMapper.getSchedulerDTO(it)
                }

                val scheduler = schedulerServiceMapper.getScheduler(schedulerDTO)

                schedulerRepository.save(scheduler)
                sendNotification(schedulerDTO, oldSchedulerDTO = oldSchedulerDTO)
            }

            EnumSchedulerType.RECURRENT -> {
                throw IllegalArgumentException(
                    messageSource.getMessage("scheduler.error.recurrent.with.common.save", null, Locale.getDefault())
                )
            }
        }
    }

    @CacheEvict(cacheNames = [SCHEDULER_IMPORT_CACHE_NAME], allEntries = true)
    fun saveRecurrentScheduler(recurrentSchedulerDTO: ValidatedRecurrentSchedulerDTO) {
        val schedules = recurrentSchedulerDTO.schedules.map(schedulerServiceMapper::getScheduler)
        validateConflictRecurrent(schedules)
        schedulerRepository.saveAll(schedules)

        val workout = workoutServiceMapper.getWorkout(recurrentSchedulerDTO.workoutDTO!!)
        workoutRepository.save(workout)

        val workoutGrupos = recurrentSchedulerDTO.workoutGroups.map(workoutServiceMapper::getWorkoutGroup)
        workoutGroupRepository.saveAll(workoutGrupos)

        val firstScheduler = schedules.first()
        val notificationSchedulerDTO = ValidatedSchedulerDTO(
            id = firstScheduler.id,
            professionalPersonId = firstScheduler.professionalPerson?.id,
            academyMemberPersonId = firstScheduler.academyMemberPerson?.id,
            dateTimeStart = firstScheduler.dateTimeStart,
            type = EnumSchedulerType.RECURRENT
        )

        sendNotification(notificationSchedulerDTO, scheduledDates = schedules.map { it.dateTimeStart!!.toLocalDate() })
    }

    private fun sendNotification(
        schedulerDTO: ValidatedSchedulerDTO,
        oldSchedulerDTO: ValidatedSchedulerDTO? = null,
        scheduledDates: List<LocalDate> = emptyList()
    ) {
        if (oldSchedulerDTO != null) {

            when {
                oldSchedulerDTO.situation == EnumSchedulerSituation.SCHEDULED && schedulerDTO.situation == EnumSchedulerSituation.CONFIRMED -> {
                    notifyMemberSchedulerConfirmed(schedulerDTO)
                }

                oldSchedulerDTO.situation != EnumSchedulerSituation.CANCELLED && schedulerDTO.situation == EnumSchedulerSituation.CANCELLED -> {
                    notifyMemberSchedulerCancelled(schedulerDTO)
                }

                oldSchedulerDTO.dateTimeStart != schedulerDTO.dateTimeStart || oldSchedulerDTO.dateTimeEnd != schedulerDTO.dateTimeEnd -> {
                    notifyMemberSchedulerTimeChanged(schedulerDTO)
                }
            }
        } else {
            when (schedulerDTO.type!!) {
                EnumSchedulerType.SUGGESTION -> {
                    notifyProfessionalNewSuggestionScheduler(schedulerDTO)
                }

                EnumSchedulerType.UNIQUE -> {
                    notifyMemberNewUniqueScheduler(schedulerDTO)
                }

                EnumSchedulerType.RECURRENT -> {
                    notifyMemberNewRecurrentScheduler(schedulerDTO, scheduledDates)
                }
            }
        }
    }

    private fun notifyMemberSchedulerConfirmed(schedulerDTO: ValidatedSchedulerDTO) {
        val zoneId = deviceService.getDeviceFromPerson(schedulerDTO.academyMemberPersonId!!)?.zoneId!!
        val date = schedulerDTO.dateTimeStart?.format(EnumDateTimePatterns.DAY_MONTH, ZoneId.of(zoneId))
        val start = schedulerDTO.dateTimeStart?.format(EnumDateTimePatterns.TIME, ZoneId.of(zoneId))
        val end = schedulerDTO.dateTimeEnd?.format(EnumDateTimePatterns.TIME, ZoneId.of(zoneId))
        val professionalName = getProfessionalFirstName(schedulerDTO)

        val data = SchedulerNotificationCustomData(
            recurrent = false,
            schedulerId = schedulerDTO.id!!,
            schedulerDate = schedulerDTO.dateTimeStart!!.toLocalDate()
        )

        firebaseNotificationService.sendNotificationToPerson(
            title = messageSource.getMessage("scheduler.confirmed.notification.title", null, Locale.getDefault()),
            message = messageSource.getMessage("scheduler.confirmed.notification.message", arrayOf(date, start, end, professionalName), Locale.getDefault()),
            personIds = listOf(schedulerDTO.academyMemberPersonId!!),
            channel = EnumNotificationChannel.SCHEDULER_CHANNEL,
            customJSONData = GsonBuilder().defaultGSon().toJson(data)
        )
    }

    private fun notifyMemberSchedulerCancelled(schedulerDTO: ValidatedSchedulerDTO) {
        val cancellationPerson = personRepository.findById(schedulerDTO.cancellationPersonId!!).get()
        val personIdToNotify = if (cancellationPerson.id == schedulerDTO.professionalPersonId) {
            schedulerDTO.academyMemberPersonId
        } else {
            schedulerDTO.professionalPersonId
        }

        val zoneId = deviceService.getDeviceFromPerson(personIdToNotify!!)?.zoneId!!
        val date = schedulerDTO.dateTimeStart?.format(EnumDateTimePatterns.DAY_MONTH, ZoneId.of(zoneId))
        val start = schedulerDTO.dateTimeStart?.format(EnumDateTimePatterns.TIME, ZoneId.of(zoneId))
        val end = schedulerDTO.dateTimeEnd?.format(EnumDateTimePatterns.TIME, ZoneId.of(zoneId))
        val personName = getPersonFirstName(cancellationPerson)

        val data = SchedulerNotificationCustomData(
            recurrent = false,
            schedulerId = schedulerDTO.id!!,
            schedulerDate = schedulerDTO.dateTimeStart!!.toLocalDate()
        )

        firebaseNotificationService.sendNotificationToPerson(
            title = messageSource.getMessage("scheduler.canceled.notification.title", null, Locale.getDefault()),
            message = messageSource.getMessage("scheduler.canceled.notification.message", arrayOf(date, start, end, personName), Locale.getDefault()),
            personIds = listOf(personIdToNotify),
            channel = EnumNotificationChannel.SCHEDULER_CHANNEL,
            customJSONData = GsonBuilder().defaultGSon().toJson(data)
        )
    }

    private fun getPersonFirstName(person: Person): String? {
        return person.name?.split(" ")?.firstOrNull()
    }

    private fun notifyMemberSchedulerTimeChanged(schedulerDTO: ValidatedSchedulerDTO) {
        val zoneId = deviceService.getDeviceFromPerson(schedulerDTO.academyMemberPersonId!!)?.zoneId!!
        val date = schedulerDTO.dateTimeStart?.format(EnumDateTimePatterns.DAY_MONTH, ZoneId.of(zoneId))

        val data = SchedulerNotificationCustomData(
            recurrent = false,
            schedulerId = schedulerDTO.id!!,
            schedulerDate = schedulerDTO.dateTimeStart!!.toLocalDate()
        )

        firebaseNotificationService.sendNotificationToPerson(
            title = messageSource.getMessage("scheduler.changed.notification.title", null, Locale.getDefault()),
            message = messageSource.getMessage("scheduler.changed.notification.message", arrayOf(date), Locale.getDefault()),
            personIds = listOf(schedulerDTO.academyMemberPersonId!!),
            channel = EnumNotificationChannel.SCHEDULER_CHANNEL,
            customJSONData = GsonBuilder().defaultGSon().toJson(data)
        )
    }

    private fun notifyProfessionalNewSuggestionScheduler(schedulerDTO: ValidatedSchedulerDTO) {
        val zoneId = deviceService.getDeviceFromPerson(schedulerDTO.professionalPersonId!!)?.zoneId!!
        val date = schedulerDTO.dateTimeStart?.format(EnumDateTimePatterns.DAY_MONTH, ZoneId.of(zoneId))
        val start = schedulerDTO.dateTimeStart?.format(EnumDateTimePatterns.TIME, ZoneId.of(zoneId))
        val end = schedulerDTO.dateTimeEnd?.format(EnumDateTimePatterns.TIME, ZoneId.of(zoneId))
        val memberName = getMemberFirstName(schedulerDTO)

        val data = SchedulerNotificationCustomData(
            recurrent = false,
            schedulerId = schedulerDTO.id!!,
            schedulerDate = schedulerDTO.dateTimeStart!!.toLocalDate()
        )

        firebaseNotificationService.sendNotificationToPerson(
            title = messageSource.getMessage("scheduler.new.suggestion.notification.title", null, Locale.getDefault()),
            message = messageSource.getMessage("scheduler.new.suggestion.notification.message", arrayOf(memberName, date, start, end), Locale.getDefault()),
            personIds = listOf(schedulerDTO.professionalPersonId!!),
            channel = EnumNotificationChannel.SCHEDULER_CHANNEL,
            customJSONData = GsonBuilder().defaultGSon().toJson(data)
        )
    }

    private fun getMemberFirstName(schedulerDTO: ValidatedSchedulerDTO): String? {
        val person = personRepository.findById(schedulerDTO.academyMemberPersonId!!).get()

        return getPersonFirstName(person)
    }

    private fun notifyMemberNewUniqueScheduler(schedulerDTO: ValidatedSchedulerDTO) {
        val zoneId = deviceService.getDeviceFromPerson(schedulerDTO.academyMemberPersonId!!)?.zoneId!!
        val date = schedulerDTO.dateTimeStart?.format(EnumDateTimePatterns.DAY_MONTH, ZoneId.of(zoneId))
        val start = schedulerDTO.dateTimeStart?.format(EnumDateTimePatterns.TIME, ZoneId.of(zoneId))
        val end = schedulerDTO.dateTimeEnd?.format(EnumDateTimePatterns.TIME, ZoneId.of(zoneId))
        val professionalName = getProfessionalFirstName(schedulerDTO)

        val data = SchedulerNotificationCustomData(
            recurrent = false,
            schedulerId = schedulerDTO.id!!,
            schedulerDate = schedulerDTO.dateTimeStart!!.toLocalDate()
        )

        firebaseNotificationService.sendNotificationToPerson(
            title = messageSource.getMessage("scheduler.new.notification.title", null, Locale.getDefault()),
            message = messageSource.getMessage("scheduler.new.notification.message", arrayOf(professionalName, date, start, end), Locale.getDefault()),
            personIds = listOf(schedulerDTO.academyMemberPersonId!!),
            channel = EnumNotificationChannel.SCHEDULER_CHANNEL,
            customJSONData = GsonBuilder().defaultGSon().toJson(data)
        )
    }

    private fun notifyMemberNewRecurrentScheduler(schedulerDTO: ValidatedSchedulerDTO, scheduledDates: List<LocalDate>) {
        val professionalName = getProfessionalFirstName(schedulerDTO)

        val dateCount = scheduledDates.size.toString()
        val start = scheduledDates.first().format(EnumDateTimePatterns.DAY_MONTH)
        val end = scheduledDates.last().format(EnumDateTimePatterns.DAY_MONTH)

        val data = SchedulerNotificationCustomData(
            recurrent = true,
            schedulerId = schedulerDTO.id,
            schedulerDate = schedulerDTO.dateTimeStart!!.toLocalDate()
        )

        firebaseNotificationService.sendNotificationToPerson(
            title = messageSource.getMessage("scheduler.new.recurrent.notification.title", null, Locale.getDefault()),
            message = messageSource.getMessage("scheduler.new.recurrent.notification.message", arrayOf(professionalName, dateCount, start, end), Locale.getDefault()),
            personIds = listOf(schedulerDTO.academyMemberPersonId!!),
            channel = EnumNotificationChannel.SCHEDULER_CHANNEL,
            customJSONData = GsonBuilder().defaultGSon().toJson(data)
        )
    }

    private fun getProfessionalFirstName(schedulerDTO: ValidatedSchedulerDTO): String? {
        val person = personRepository.findById(schedulerDTO.professionalPersonId!!).get()
        return getPersonFirstName(person)
    }

    @Throws(BusinessException::class)
    private fun validateScheduler(dto: ISchedulerDTO) {
        val scheduler = schedulerServiceMapper.getScheduler(dto)

        when (dto.type!!) {
            EnumSchedulerType.SUGGESTION -> {
                validateConflict(scheduler, scheduler.professionalPerson!!)
                validateStartTimeSuggestionScheduler(scheduler)
                validateEndTimeSuggestionScheduler(scheduler)
                validateTimePeriod(scheduler)
            }

            EnumSchedulerType.UNIQUE -> {
                validateConflict(scheduler, scheduler.academyMemberPerson!!)
                validateStartTime(scheduler)
                validateTimePeriod(scheduler)
            }

            EnumSchedulerType.RECURRENT -> {
            }
        }
    }

    @Throws(BusinessException::class)
    private fun validateConflict(scheduler: Scheduler, person: Person) {
        val hasConflict = customSchedulerRepository.getHasSchedulerConflict(
            schedulerId = scheduler.id,
            personId = person.id,
            userType = person.user?.type!!,
            start = scheduler.dateTimeStart!!,
            end = scheduler.dateTimeEnd!!
        )

        if (hasConflict) {
            throw BusinessException(
                messageSource.getMessage(
                    "scheduler.error.conflict",
                    arrayOf(
                        scheduler.dateTimeStart!!.format(EnumDateTimePatterns.DATE),
                        scheduler.dateTimeStart!!.format(EnumDateTimePatterns.TIME),
                        scheduler.dateTimeEnd!!.format(EnumDateTimePatterns.TIME),
                        scheduler.professionalPerson?.name
                    ),
                    Locale.getDefault()
                )
            )
        }
    }

    private fun validateConflictRecurrent(schedulers: List<Scheduler>) {
        val conflicts = schedulers.filter { scheduler ->
            customSchedulerRepository.getHasSchedulerConflict(
                schedulerId = scheduler.id,
                personId = scheduler.academyMemberPerson?.id!!,
                userType = scheduler.academyMemberPerson?.user?.type!!,
                start = scheduler.dateTimeStart!!,
                end = scheduler.dateTimeEnd!!
            )
        }

        if (conflicts.isNotEmpty()) {
            val formatedDates = conflicts.joinToString(separator = ", \n") { it.dateTimeStart!!.format(
                EnumDateTimePatterns.DATE
            ) }

            throw BusinessException(
                messageSource.getMessage(
                    "scheduler.error.recurrent.conflicts",
                    arrayOf(formatedDates),
                    Locale.getDefault()
                )
            )
        }
    }

    private fun validateStartTime(scheduler: Scheduler) {
        val dateTimeStart = scheduler.dateTimeStart!!
        val actualHour = timeNow(dateTimeStart.toZonedDateTime()?.zone!!)
        val timeStart = dateTimeStart.toLocalTime()

        when {
            timeStart <= actualHour.plusHours(1) && dateTimeStart.toLocalDate() == dateNow() -> {
                throw BusinessException(
                    messageSource.getMessage("scheduler.error.start.time.antecedence", null, Locale.getDefault())
                )
            }
        }
    }

    private fun validateStartTimeSuggestionScheduler(scheduler: Scheduler) {
        validateStartTime(scheduler)

        val academyTimes = customAcademyRepository.getPersonAcademyTimeList(
            personId = scheduler.professionalPerson?.id!!,
            dayOfWeek = scheduler.dateTimeStart?.toLocalDate()?.dayOfWeek!!
        )

        val startWorkTime = academyTimes.minOf { it.timeStart!! }
        val endWorkTime = academyTimes.maxOf { it.timeEnd!! }

        if (scheduler.dateTimeStart!!.toLocalTime() < startWorkTime || scheduler.dateTimeStart!!.toLocalTime() > endWorkTime) {
            val start = startWorkTime.format(EnumDateTimePatterns.TIME)
            val end = endWorkTime.format(EnumDateTimePatterns.TIME)

            throw BusinessException(
                messageSource.getMessage(
                    "scheduler.error.start.time.between.worktime",
                    arrayOf(start, end),
                    Locale.getDefault()
                )
            )
        }
    }

    private fun validateEndTimeSuggestionScheduler(scheduler: Scheduler) {
        val academyTimes = customAcademyRepository.getPersonAcademyTimeList(
            personId = scheduler.professionalPerson?.id!!,
            dayOfWeek = scheduler.dateTimeStart?.toLocalDate()?.dayOfWeek!!
        )

        val startWorkTime = academyTimes.minOf { it.timeStart!! }
        val endWorkTime = academyTimes.maxOf { it.timeEnd!! }

        if (scheduler.dateTimeEnd!!.toLocalTime() < startWorkTime || scheduler.dateTimeEnd!!.toLocalTime() > endWorkTime) {
            val start = startWorkTime.format(EnumDateTimePatterns.TIME)
            val end = endWorkTime.format(EnumDateTimePatterns.TIME)

            throw BusinessException(
                messageSource.getMessage(
                    "scheduler.error.end.time.between.worktime",
                    arrayOf(start, end),
                    Locale.getDefault()
                )
            )
        }
    }

    private fun validateTimePeriod(scheduler: Scheduler) {
        if (scheduler.dateTimeStart!!.isAfter(scheduler.dateTimeEnd!!) || scheduler.dateTimeStart == scheduler.dateTimeEnd) {
            throw BusinessException(
                messageSource.getMessage(
                    "scheduler.error.invalid.period",
                    null,
                    Locale.getDefault()
                )
            )
        }
    }

    @CacheEvict(cacheNames = [SCHEDULER_IMPORT_CACHE_NAME], allEntries = true)
    fun saveSchedulerBatch(list: List<ISchedulerDTO>) {
        if (list.any { it.type == EnumSchedulerType.RECURRENT }) {
            throw BusinessException(
                messageSource.getMessage(
                    "scheduler.error.recurrent.batch",
                    null,
                    Locale.getDefault()
                )
            )
        }

        val schedules = list.map { schedulerDTO ->
            validateScheduler(schedulerDTO)
            schedulerServiceMapper.getScheduler(schedulerDTO)
        }

        schedulerRepository.saveAll(schedules)
    }

    @Cacheable(cacheNames = [SCHEDULER_IMPORT_CACHE_NAME], key = "#filter.toCacheKey()")
    fun getSchedulesImport(filter: CommonImportFilter, pageInfos: ImportPageInfos): List<ValidatedSchedulerDTO> {
        return customSchedulerRepository.getSchedulesImport(filter, pageInfos).map(schedulerServiceMapper::getSchedulerDTO)
    }

    fun updateSchedulersNotified(schedulerIdsSuccess: List<String>) {
        val schedulers = schedulerRepository.findAllById(schedulerIdsSuccess).onEach {
            it.notifiedAntecedence = true
        }

        schedulerRepository.saveAll(schedulers)
    }

}