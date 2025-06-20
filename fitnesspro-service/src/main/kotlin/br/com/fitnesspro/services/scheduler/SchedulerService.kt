package br.com.fitnesspro.services.scheduler

import br.com.fitnesspro.config.application.cache.SCHEDULER_CONFIG_IMPORT_CACHE_NAME
import br.com.fitnesspro.config.application.cache.SCHEDULER_IMPORT_CACHE_NAME
import br.com.fitnesspro.config.gson.defaultGSon
import br.com.fitnesspro.core.enums.EnumDateTimePatterns.*
import br.com.fitnesspro.core.extensions.dateNow
import br.com.fitnesspro.core.extensions.format
import br.com.fitnesspro.core.extensions.timeNow
import br.com.fitnesspro.exception.BusinessException
import br.com.fitnesspro.models.general.Person
import br.com.fitnesspro.models.scheduler.Scheduler
import br.com.fitnesspro.models.scheduler.SchedulerConfig
import br.com.fitnesspro.models.workout.Workout
import br.com.fitnesspro.models.workout.WorkoutGroup
import br.com.fitnesspro.repository.general.academy.ICustomAcademyRepository
import br.com.fitnesspro.repository.general.person.IPersonRepository
import br.com.fitnesspro.repository.scheduler.ICustomSchedulerConfigRepository
import br.com.fitnesspro.repository.scheduler.ICustomSchedulerRepository
import br.com.fitnesspro.repository.scheduler.ISchedulerConfigRepository
import br.com.fitnesspro.repository.scheduler.ISchedulerRepository
import br.com.fitnesspro.repository.workout.IWorkoutGroupRepository
import br.com.fitnesspro.repository.workout.IWorkoutRepository
import br.com.fitnesspro.services.firebase.FirebaseNotificationService
import br.com.fitnesspro.services.mappers.SchedulerServiceMapper
import br.com.fitnesspro.services.serviceauth.DeviceService
import br.com.fitnesspro.shared.communication.dtos.scheduler.RecurrentConfigDTO
import br.com.fitnesspro.shared.communication.dtos.scheduler.SchedulerConfigDTO
import br.com.fitnesspro.shared.communication.dtos.scheduler.SchedulerDTO
import br.com.fitnesspro.shared.communication.enums.notification.EnumNotificationChannel
import br.com.fitnesspro.shared.communication.enums.scheduler.EnumSchedulerSituation.*
import br.com.fitnesspro.shared.communication.enums.scheduler.EnumSchedulerType
import br.com.fitnesspro.shared.communication.notification.SchedulerNotificationCustomData
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.CommonImportFilter
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
    private val schedulerConfigRepository: ISchedulerConfigRepository,
    private val personRepository: IPersonRepository,
    private val customAcademyRepository: ICustomAcademyRepository,
    private val customSchedulerConfigRepository: ICustomSchedulerConfigRepository,
    private val workoutRepository: IWorkoutRepository,
    private val workoutGroupRepository: IWorkoutGroupRepository,
    private val firebaseNotificationService: FirebaseNotificationService,
    private val deviceService: DeviceService,
    private val schedulerServiceMapper: SchedulerServiceMapper,
    private val messageSource: MessageSource
) {
    @CacheEvict(cacheNames = [SCHEDULER_IMPORT_CACHE_NAME], allEntries = true)
    fun saveScheduler(schedulerDTO: SchedulerDTO) {
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
                val config = schedulerDTO.recurrentConfig!!

                val scheduleDates = generateSequence(config.dateStart) { it.plusDays(1) }
                    .takeWhile { it <= config.dateEnd }
                    .filter { config.dayWeeks.contains(it.dayOfWeek) }
                    .toList()

                val schedules = scheduleDates.map { date ->
                    val newStart = schedulerDTO.dateTimeStart!!
                        .withYear(date.year)
                        .withMonth(date.monthValue)
                        .withDayOfMonth(date.dayOfMonth)

                    val newEnd = schedulerDTO.dateTimeEnd!!
                        .withYear(date.year)
                        .withMonth(date.monthValue)
                        .withDayOfMonth(date.dayOfMonth)

                    schedulerServiceMapper.getScheduler(schedulerDTO).copy(
                        dateTimeStart = newStart,
                        dateTimeEnd = newEnd
                    )
                }

                validateConflictRecurrent(schedules)

                val firstScheduler = schedules.first()

                val workout = Workout(
                    academyMemberPerson = firstScheduler.academyMemberPerson,
                    professionalPerson = firstScheduler.professionalPerson,
                    dateStart = firstScheduler.dateTimeStart?.toLocalDate(),
                    dateEnd = schedules.last().dateTimeStart?.toLocalDate(),
                )

                val workoutGroups = schedules.map { it.dateTimeStart?.toLocalDate()!!.dayOfWeek }.distinct().map {
                    WorkoutGroup(
                        dayWeek = it,
                        workout = workout,
                    )
                }


                schedulerRepository.saveAll(schedules)
                workoutRepository.save(workout)
                workoutGroupRepository.saveAll(workoutGroups)

                sendNotification(schedulerDTO, scheduledDates = scheduleDates)
            }
        }
    }

    private fun sendNotification(
        schedulerDTO: SchedulerDTO,
        oldSchedulerDTO: SchedulerDTO? = null,
        scheduledDates: List<LocalDate> = emptyList()
    ) {
        if (oldSchedulerDTO != null) {

            when {
                oldSchedulerDTO.situation == SCHEDULED && schedulerDTO.situation == CONFIRMED -> {
                    notifyMemberSchedulerConfirmed(schedulerDTO)
                }

                oldSchedulerDTO.situation != CANCELLED && schedulerDTO.situation == CANCELLED -> {
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

    private fun notifyMemberSchedulerConfirmed(schedulerDTO: SchedulerDTO) {
        val zoneId = deviceService.getDeviceFromPerson(schedulerDTO.academyMemberPersonId!!)?.zoneId!!
        val date = schedulerDTO.dateTimeStart?.format(DAY_MONTH, ZoneId.of(zoneId))
        val start = schedulerDTO.dateTimeStart?.format(TIME, ZoneId.of(zoneId))
        val end = schedulerDTO.dateTimeEnd?.format(TIME, ZoneId.of(zoneId))
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

    private fun notifyMemberSchedulerCancelled(schedulerDTO: SchedulerDTO) {
        val cancellationPerson = personRepository.findById(schedulerDTO.cancellationPersonId!!).get()
        val personIdToNotify = if (cancellationPerson.id == schedulerDTO.professionalPersonId) {
            schedulerDTO.academyMemberPersonId
        } else {
            schedulerDTO.professionalPersonId
        }

        val zoneId = deviceService.getDeviceFromPerson(personIdToNotify!!)?.zoneId!!
        val date = schedulerDTO.dateTimeStart?.format(DAY_MONTH, ZoneId.of(zoneId))
        val start = schedulerDTO.dateTimeStart?.format(TIME, ZoneId.of(zoneId))
        val end = schedulerDTO.dateTimeEnd?.format(TIME, ZoneId.of(zoneId))
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

    private fun notifyMemberSchedulerTimeChanged(schedulerDTO: SchedulerDTO) {
        val zoneId = deviceService.getDeviceFromPerson(schedulerDTO.academyMemberPersonId!!)?.zoneId!!
        val date = schedulerDTO.dateTimeStart?.format(DAY_MONTH, ZoneId.of(zoneId))

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

    private fun notifyProfessionalNewSuggestionScheduler(schedulerDTO: SchedulerDTO) {
        val zoneId = deviceService.getDeviceFromPerson(schedulerDTO.professionalPersonId!!)?.zoneId!!
        val date = schedulerDTO.dateTimeStart?.format(DAY_MONTH, ZoneId.of(zoneId))
        val start = schedulerDTO.dateTimeStart?.format(TIME, ZoneId.of(zoneId))
        val end = schedulerDTO.dateTimeEnd?.format(TIME, ZoneId.of(zoneId))
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

    private fun getMemberFirstName(schedulerDTO: SchedulerDTO): String? {
        val person = personRepository.findById(schedulerDTO.academyMemberPersonId!!).get()

        return getPersonFirstName(person)
    }

    private fun notifyMemberNewUniqueScheduler(schedulerDTO: SchedulerDTO) {
        val zoneId = deviceService.getDeviceFromPerson(schedulerDTO.academyMemberPersonId!!)?.zoneId!!
        val date = schedulerDTO.dateTimeStart?.format(DAY_MONTH, ZoneId.of(zoneId))
        val start = schedulerDTO.dateTimeStart?.format(TIME, ZoneId.of(zoneId))
        val end = schedulerDTO.dateTimeEnd?.format(TIME, ZoneId.of(zoneId))
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

    private fun notifyMemberNewRecurrentScheduler(schedulerDTO: SchedulerDTO, scheduledDates: List<LocalDate>) {
        val professionalName = getProfessionalFirstName(schedulerDTO)

        val dateCount = scheduledDates.size.toString()
        val start = scheduledDates.first().format(DAY_MONTH)
        val end = scheduledDates.last().format(DAY_MONTH)

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

    private fun getProfessionalFirstName(schedulerDTO: SchedulerDTO): String? {
        val person = personRepository.findById(schedulerDTO.professionalPersonId!!).get()
        return getPersonFirstName(person)
    }

    @Throws(BusinessException::class)
    private fun validateScheduler(dto: SchedulerDTO) {
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
                validateStartTime(scheduler)
                validateTimePeriod(scheduler)
                validateDateConfigPeriod(dto.recurrentConfig!!)
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
                        scheduler.dateTimeStart!!.format(DATE),
                        scheduler.dateTimeStart!!.format(TIME),
                        scheduler.dateTimeEnd!!.format(TIME),
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
            val formatedDates = conflicts.joinToString(separator = ", \n") { it.dateTimeStart!!.format(DATE) }

            throw BusinessException(
                messageSource.getMessage("scheduler.error.recurrent.conflicts", arrayOf(formatedDates), Locale.getDefault())
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
            val start = startWorkTime.format(TIME)
            val end = endWorkTime.format(TIME)

            throw BusinessException(
                messageSource.getMessage("scheduler.error.start.time.between.worktime", arrayOf(start, end), Locale.getDefault())
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
            val start = startWorkTime.format(TIME)
            val end = endWorkTime.format(TIME)

            throw BusinessException(
                messageSource.getMessage("scheduler.error.end.time.between.worktime", arrayOf(start, end), Locale.getDefault())
            )
        }
    }

    private fun validateTimePeriod(scheduler: Scheduler) {
        if (scheduler.dateTimeStart!!.isAfter(scheduler.dateTimeEnd!!) || scheduler.dateTimeStart == scheduler.dateTimeEnd) {
            throw BusinessException(messageSource.getMessage("scheduler.error.invalid.period", null, Locale.getDefault()))
        }
    }

    private fun validateDateConfigPeriod(recurrentConfig: RecurrentConfigDTO) {
        if (recurrentConfig.dateStart.isAfter(recurrentConfig.dateEnd) ||
            recurrentConfig.dateStart == recurrentConfig.dateEnd) {
            throw BusinessException(messageSource.getMessage("scheduler.error.invalid.recurrent.period", null, Locale.getDefault()))
        }
    }

    @CacheEvict(cacheNames = [SCHEDULER_IMPORT_CACHE_NAME], allEntries = true)
    fun saveSchedulerBatch(schedulerDTOList: List<SchedulerDTO>) {
        if (schedulerDTOList.any { it.type == EnumSchedulerType.RECURRENT }) {
            throw BusinessException(messageSource.getMessage("scheduler.error.recurrent.batch", null, Locale.getDefault()))
        }

        val schedules = schedulerDTOList.map { schedulerDTO ->
            validateScheduler(schedulerDTO)
            schedulerServiceMapper.getScheduler(schedulerDTO)
        }

        schedulerRepository.saveAll(schedules)
    }

    @CacheEvict(cacheNames = [SCHEDULER_CONFIG_IMPORT_CACHE_NAME], allEntries = true)
    fun saveSchedulerConfig(schedulerConfigDTO: SchedulerConfigDTO) {
        val config = schedulerServiceMapper.getSchedulerConfig(schedulerConfigDTO)

        validateDensityRange(config)

        schedulerConfigRepository.save(config)
    }

    private fun validateDensityRange(config: SchedulerConfig) {
        if (config.minScheduleDensity > config.maxScheduleDensity ||
            config.minScheduleDensity == config.maxScheduleDensity) {
            throw BusinessException(messageSource.getMessage("scheduler.config.error.invalid.density.range", null, Locale.getDefault()))
        }
    }

    @CacheEvict(cacheNames = [SCHEDULER_CONFIG_IMPORT_CACHE_NAME], allEntries = true)
    fun saveSchedulerConfigBatch(schedulerConfigDTOList: List<SchedulerConfigDTO>) {
        val configs = schedulerConfigDTOList.map {
            val config = schedulerServiceMapper.getSchedulerConfig(it)
            validateDensityRange(config)

            config
        }

        schedulerConfigRepository.saveAll(configs)
    }

    @Cacheable(cacheNames = [SCHEDULER_IMPORT_CACHE_NAME], key = "#filter.toCacheKey()")
    fun getSchedulesImport(filter: CommonImportFilter, pageInfos: ImportPageInfos): List<SchedulerDTO> {
        return customSchedulerRepository.getSchedulesImport(filter, pageInfos).map(schedulerServiceMapper::getSchedulerDTO)
    }

    @Cacheable(cacheNames = [SCHEDULER_CONFIG_IMPORT_CACHE_NAME], key = "#filter.toCacheKey()")
    fun getSchedulerConfigsImport(filter: CommonImportFilter, pageInfos: ImportPageInfos): List<SchedulerConfigDTO> {
        return customSchedulerConfigRepository.getSchedulerConfigImport(filter, pageInfos).map(schedulerServiceMapper::getSchedulerConfigDTO)
    }

    fun updateSchedulersNotified(schedulerIdsSuccess: List<String>) {
        val schedulers = schedulerRepository.findAllById(schedulerIdsSuccess).onEach {
            it.notifiedAntecedence = true
        }

        schedulerRepository.saveAll(schedulers)
    }

}