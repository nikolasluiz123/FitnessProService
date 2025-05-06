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
import br.com.fitnesspro.shared.communication.query.filter.CommonImportFilter
import com.google.gson.GsonBuilder
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.ZoneId

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
    private val schedulerServiceMapper: SchedulerServiceMapper
) {
    @CacheEvict(cacheNames = [SCHEDULER_IMPORT_CACHE_NAME], allEntries = true)
    fun saveScheduler(schedulerDTO: SchedulerDTO) {
        validateScheduler(schedulerDTO)

        when (schedulerDTO.type!!) {
            EnumSchedulerType.SUGGESTION, EnumSchedulerType.UNIQUE -> {
                val scheduler = schedulerServiceMapper.getScheduler(schedulerDTO)
                sendNotification(schedulerDTO)
                schedulerRepository.save(scheduler)
            }

            EnumSchedulerType.RECURRENT -> {
                val config = schedulerDTO.recurrentConfig!!

                val scheduleDates = generateSequence(config.dateStart) { it.plusDays(1) }
                    .takeWhile { it <= config.dateEnd }
                    .filter { config.dayWeeks.contains(it.dayOfWeek) }
                    .toList()

                val schedules = scheduleDates.map { date ->
                    schedulerServiceMapper.getScheduler(schedulerDTO).copy(scheduledDate = date)
                }

                validateConflictRecurrent(schedules)

                val firstScheduler = schedules.first()

                val workout = Workout(
                    academyMemberPerson = firstScheduler.academyMemberPerson,
                    professionalPerson = firstScheduler.professionalPerson,
                    dateStart = firstScheduler.scheduledDate,
                    dateEnd = schedules.last().scheduledDate,
                )

                val workoutGroups = schedules.map { it.scheduledDate!!.dayOfWeek }.distinct().map {
                    WorkoutGroup(
                        dayWeek = it,
                        workout = workout,
                    )
                }

                sendNotification(schedulerDTO, scheduleDates)

                schedulerRepository.saveAll(schedules)
                workoutRepository.save(workout)
                workoutGroupRepository.saveAll(workoutGroups)
            }
        }
    }

    private fun sendNotification(schedulerDTO: SchedulerDTO, scheduledDates: List<LocalDate> = emptyList()) {
        val schedulerOptional = schedulerDTO.id?.let { schedulerRepository.findById(it) }

        if (schedulerOptional?.isPresent == true) {
            val scheduler = schedulerOptional.get()

            when {
                scheduler.situation == SCHEDULED && schedulerDTO.situation == CONFIRMED -> {
                    notifyMemberSchedulerConfirmed(schedulerDTO)
                }

                scheduler.situation != CANCELLED && schedulerDTO.situation == CANCELLED -> {
                    notifyMemberSchedulerCancelled(schedulerDTO)
                }

                scheduler.timeStart != schedulerDTO.timeStart || scheduler.timeEnd != schedulerDTO.timeEnd -> {
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
        val zoneId = deviceService.getDeviceFromPerson(schedulerDTO.academyMemberPersonId!!).zoneId!!
        val date = schedulerDTO.scheduledDate?.format(DAY_MONTH)
        val start = schedulerDTO.timeStart?.format(TIME, ZoneId.of(zoneId))
        val end = schedulerDTO.timeEnd?.format(TIME, ZoneId.of(zoneId))
        val professionalName = personRepository.findById(schedulerDTO.professionalPersonId!!).get().name

        val data = SchedulerNotificationCustomData(
            recurrent = false,
            schedulerId = schedulerDTO.id!!,
            schedulerDate = schedulerDTO.scheduledDate!!
        )

        firebaseNotificationService.sendNotificationToPerson(
            title = "Agendamento confirmado",
            message = "O Agendamento de $date das $start às $end foi confirmado por ${professionalName}.",
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

        val zoneId = deviceService.getDeviceFromPerson(personIdToNotify!!).zoneId!!
        val date = schedulerDTO.scheduledDate?.format(DAY_MONTH)
        val start = schedulerDTO.timeStart?.format(TIME, ZoneId.of(zoneId))
        val end = schedulerDTO.timeEnd?.format(TIME, ZoneId.of(zoneId))

        val data = SchedulerNotificationCustomData(
            recurrent = false,
            schedulerId = schedulerDTO.id!!,
            schedulerDate = schedulerDTO.scheduledDate!!
        )

        firebaseNotificationService.sendNotificationToPerson(
            title = "Agendamento cancelado",
            message = "O Agendamento de $date das $start às $end foi cancelado por ${cancellationPerson.name}.",
            personIds = listOf(personIdToNotify),
            channel = EnumNotificationChannel.SCHEDULER_CHANNEL,
            customJSONData = GsonBuilder().defaultGSon().toJson(data)
        )
    }

    private fun notifyMemberSchedulerTimeChanged(schedulerDTO: SchedulerDTO) {
        val date = schedulerDTO.scheduledDate?.format(DAY_MONTH)

        val data = SchedulerNotificationCustomData(
            recurrent = false,
            schedulerId = schedulerDTO.id!!,
            schedulerDate = schedulerDTO.scheduledDate!!
        )

        firebaseNotificationService.sendNotificationToPerson(
            title = "Alteração no horário de um agendamento",
            message = "O Agendamento de $date teve o horário alterado, verifique o novo horário.",
            personIds = listOf(schedulerDTO.academyMemberPersonId!!),
            channel = EnumNotificationChannel.SCHEDULER_CHANNEL,
            customJSONData = GsonBuilder().defaultGSon().toJson(data)
        )
    }

    private fun notifyProfessionalNewSuggestionScheduler(schedulerDTO: SchedulerDTO) {
        val zoneId = deviceService.getDeviceFromPerson(schedulerDTO.professionalPersonId!!).zoneId!!
        val date = schedulerDTO.scheduledDate?.format(DAY_MONTH)
        val start = schedulerDTO.timeStart?.format(TIME, ZoneId.of(zoneId))
        val end = schedulerDTO.timeEnd?.format(TIME, ZoneId.of(zoneId))
        val memberName = personRepository.findById(schedulerDTO.academyMemberPersonId!!).get().name

        val data = SchedulerNotificationCustomData(
            recurrent = false,
            schedulerId = schedulerDTO.id!!,
            schedulerDate = schedulerDTO.scheduledDate!!
        )

        firebaseNotificationService.sendNotificationToPerson(
            title = "Nova sugestão de agendamento",
            message = "$memberName sugeriu um agendamento para o dia $date das $start às $end.",
            personIds = listOf(schedulerDTO.professionalPersonId!!),
            channel = EnumNotificationChannel.SCHEDULER_CHANNEL,
            customJSONData = GsonBuilder().defaultGSon().toJson(data)
        )
    }

    private fun notifyMemberNewUniqueScheduler(schedulerDTO: SchedulerDTO) {
        val zoneId = deviceService.getDeviceFromPerson(schedulerDTO.academyMemberPersonId!!).zoneId!!
        val date = schedulerDTO.scheduledDate?.format(DAY_MONTH)
        val start = schedulerDTO.timeStart?.format(TIME, ZoneId.of(zoneId))
        val end = schedulerDTO.timeEnd?.format(TIME, ZoneId.of(zoneId))
        val professionalName = personRepository.findById(schedulerDTO.professionalPersonId!!).get().name

        val data = SchedulerNotificationCustomData(
            recurrent = false,
            schedulerId = schedulerDTO.id!!,
            schedulerDate = schedulerDTO.scheduledDate!!
        )

        firebaseNotificationService.sendNotificationToPerson(
            title = "Novo compromisso em sua agenda",
            message = "$professionalName realizou um agendamento para o dia $date das $start às $end.",
            personIds = listOf(schedulerDTO.academyMemberPersonId!!),
            channel = EnumNotificationChannel.SCHEDULER_CHANNEL,
            customJSONData = GsonBuilder().defaultGSon().toJson(data)
        )
    }

    private fun notifyMemberNewRecurrentScheduler(schedulerDTO: SchedulerDTO, scheduledDates: List<LocalDate>) {
        val professionalName = personRepository.findById(schedulerDTO.professionalPersonId!!).get().name

        val dateCount = scheduledDates.size
        val start = scheduledDates.first().format(DAY_MONTH)
        val end = scheduledDates.last().format(DAY_MONTH)

        val data = SchedulerNotificationCustomData(
            recurrent = true,
            schedulerId = schedulerDTO.id!!,
            schedulerDate = schedulerDTO.scheduledDate!!
        )

        firebaseNotificationService.sendNotificationToPerson(
            title = "Novo compromisso recorrente",
            message = "$professionalName realizou um agendamento recorrente para $dateCount datas entre $start e $end.",
            personIds = listOf(schedulerDTO.academyMemberPersonId!!),
            channel = EnumNotificationChannel.SCHEDULER_CHANNEL,
            customJSONData = GsonBuilder().defaultGSon().toJson(data)
        )
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
            scheduledDate = scheduler.scheduledDate!!,
            start = scheduler.timeStart!!,
            end = scheduler.timeEnd!!
        )

        if (hasConflict) {
            throw BusinessException(
                "Não foi possível realizar o agendamento para o dia %s das %s até %s pois ocorreu um conflito. Entre em contato com %s.".format(
                    scheduler.scheduledDate!!.format(DATE),
                    scheduler.timeStart!!.format(TIME),
                    scheduler.timeEnd!!.format(TIME),
                    scheduler.professionalPerson?.name
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
                scheduledDate = scheduler.scheduledDate!!,
                start = scheduler.timeStart!!,
                end = scheduler.timeEnd!!
            )
        }

        if (conflicts.isNotEmpty()) {
            val formatedDates = conflicts.joinToString(separator = ", \n") { it.scheduledDate!!.format(DATE) }

            throw BusinessException(
                "Não foi possível concluir o agendamento recorrente pois houveram um ou mais conflitos. As datas com compromissos conflitantes são:\\n\\n%s".format(
                    formatedDates
                )
            )
        }
    }

    private fun validateStartTime(scheduler: Scheduler) {
        val actualHour = timeNow()
        val timeStart = scheduler.timeStart!!

        when {
            timeStart <= actualHour.plusHours(1) && scheduler.scheduledDate == dateNow() -> {
                throw BusinessException("O horário de início deve ser definido com 1 hora de antecedência")
            }
        }
    }

    private fun validateStartTimeSuggestionScheduler(scheduler: Scheduler) {
        validateStartTime(scheduler)

        val academyTimes = customAcademyRepository.getPersonAcademyTimeList(
            personId = scheduler.professionalPerson?.id!!,
            dayOfWeek = scheduler.scheduledDate?.dayOfWeek!!
        )

        val startWorkTime = academyTimes.minOf { it.timeStart!! }
        val endWorkTime = academyTimes.maxOf { it.timeEnd!! }

        if (scheduler.timeStart!! < startWorkTime || scheduler.timeStart!! > endWorkTime) {
            throw BusinessException(
                "O horário de início deve ser entre %s e %s".format(
                    startWorkTime.format(TIME),
                    endWorkTime.format(TIME)
                )
            )
        }
    }

    private fun validateEndTimeSuggestionScheduler(scheduler: Scheduler) {
        val academyTimes = customAcademyRepository.getPersonAcademyTimeList(
            personId = scheduler.professionalPerson?.id!!,
            dayOfWeek = scheduler.scheduledDate?.dayOfWeek!!
        )

        val startWorkTime = academyTimes.minOf { it.timeStart!! }
        val endWorkTime = academyTimes.maxOf { it.timeEnd!! }

        if (scheduler.timeEnd!! < startWorkTime || scheduler.timeEnd!! > endWorkTime) {
            throw BusinessException(
                "O horário de fim deve ser entre %s e %s".format(startWorkTime.format(TIME), endWorkTime.format(TIME))
            )
        }
    }

    private fun validateTimePeriod(scheduler: Scheduler) {
        if (scheduler.timeStart!!.isAfter(scheduler.timeEnd!!) || scheduler.timeStart == scheduler.timeEnd) {
            throw BusinessException("Os horários de Início e Fim são inválidos")
        }
    }

    private fun validateDateConfigPeriod(recurrentConfig: RecurrentConfigDTO) {
        if (recurrentConfig.dateStart.isAfter(recurrentConfig.dateEnd) ||
            recurrentConfig.dateStart == recurrentConfig.dateEnd) {
            throw BusinessException("As datas de Início e Fim são inválidas")
        }
    }

    @CacheEvict(cacheNames = [SCHEDULER_IMPORT_CACHE_NAME], allEntries = true)
    fun saveSchedulerBatch(schedulerDTOList: List<SchedulerDTO>) {
        if (schedulerDTOList.any { it.type == EnumSchedulerType.RECURRENT }) {
            throw BusinessException("Não é possível realizar agendamentos recorrentes em lote. Tenha certeza que na lista de agendamentos não há agendamentos recorrentes.")
        }

        val schedules = schedulerDTOList.map { schedulerDTO ->
            validateScheduler(schedulerDTO)
            sendNotification(schedulerDTO)
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
            throw BusinessException("Os valores da densidade dos eventos são inválidos.")
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

}