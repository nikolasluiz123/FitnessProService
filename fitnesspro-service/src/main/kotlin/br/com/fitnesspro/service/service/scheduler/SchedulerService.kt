package br.com.fitnesspro.service.service.scheduler

import br.com.fitnesspro.core.enums.EnumDateTimePatterns.*
import br.com.fitnesspro.core.extensions.dateNow
import br.com.fitnesspro.core.extensions.format
import br.com.fitnesspro.core.extensions.timeNow
import br.com.fitnesspro.models.scheduler.enums.EnumSchedulerType
import br.com.fitnesspro.service.exception.BusinessException
import br.com.fitnesspro.service.models.general.Person
import br.com.fitnesspro.service.models.scheduler.Scheduler
import br.com.fitnesspro.service.models.scheduler.SchedulerConfig
import br.com.fitnesspro.service.models.workout.Workout
import br.com.fitnesspro.service.models.workout.WorkoutGroup
import br.com.fitnesspro.service.repository.general.academy.ICustomAcademyRepository
import br.com.fitnesspro.service.repository.general.person.IPersonRepository
import br.com.fitnesspro.service.repository.general.user.IUserRepository
import br.com.fitnesspro.service.repository.scheduler.ICustomSchedulerConfigRepository
import br.com.fitnesspro.service.repository.scheduler.ICustomSchedulerRepository
import br.com.fitnesspro.service.repository.scheduler.ISchedulerConfigRepository
import br.com.fitnesspro.service.repository.scheduler.ISchedulerRepository
import br.com.fitnesspro.service.repository.workout.IWorkoutGroupRepository
import br.com.fitnesspro.service.repository.workout.IWorkoutRepository
import br.com.fitnesspro.shared.communication.dtos.scheduler.RecurrentConfigDTO
import br.com.fitnesspro.shared.communication.dtos.scheduler.SchedulerConfigDTO
import br.com.fitnesspro.shared.communication.dtos.scheduler.SchedulerDTO
import br.com.fitnesspro.shared.communication.filter.CommonImportFilter
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import org.springframework.stereotype.Service

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
    private val userRepository: IUserRepository
) {
    fun saveScheduler(schedulerDTO: SchedulerDTO) {
        validateScheduler(schedulerDTO)

        when (schedulerDTO.type!!) {
            EnumSchedulerType.SUGGESTION, EnumSchedulerType.UNIQUE -> {
                val scheduler = schedulerDTO.toScheduler()
                schedulerRepository.save(scheduler)
            }

            EnumSchedulerType.RECURRENT -> {
                val config = schedulerDTO.recurrentConfig!!

                val scheduleDates = generateSequence(config.dateStart) { it.plusDays(1) }
                    .takeWhile { it <= config.dateEnd }
                    .filter { config.dayWeeks.contains(it.dayOfWeek) }
                    .toList()

                val schedules = scheduleDates.map { date ->
                    schedulerDTO.toScheduler().copy(scheduledDate = date)
                }

                validateConflictRecurrent(schedules)

                val firstScheduler = schedules.first()

                val workout = Workout(
                    academyMemberPerson = firstScheduler.academyMemberPerson,
                    professionalPerson = firstScheduler.professionalPerson,
                    dateStart = firstScheduler.scheduledDate,
                    dateEnd = schedules.last().scheduledDate,
                    creationUser = firstScheduler.creationUser,
                    updateUser = firstScheduler.updateUser
                )

                val workoutGroups = schedules.map { it.scheduledDate!!.dayOfWeek }.distinct().map {
                    WorkoutGroup(
                        dayWeek = it,
                        workout = workout,
                        creationUser = firstScheduler.creationUser,
                        updateUser = firstScheduler.updateUser
                    )
                }

                schedulerRepository.saveAll(schedules)
                workoutRepository.save(workout)
                workoutGroupRepository.saveAll(workoutGroups)
            }
        }
    }

    @Throws(BusinessException::class)
    private fun validateScheduler(dto: SchedulerDTO) {
        val scheduler = dto.toScheduler()

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
                "Não foi possível concluir o agendamento recorrente pois houveram um ou mais conflitos. As datas com compromissos conflitantes são:\\n\\n%s".format(formatedDates)
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
                "O horário de início deve ser entre %s e %s".format(startWorkTime.format(TIME), endWorkTime.format(TIME))
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

    fun saveSchedulerBatch(schedulerDTOList: List<SchedulerDTO>) {
        if (schedulerDTOList.any { it.type == EnumSchedulerType.RECURRENT }) {
            throw BusinessException("Não é possível realizar agendamentos recorrentes em lote. Tenha certeza que na lista de agendamentos não há agendamentos recorrentes.")
        }

        val schedules = schedulerDTOList.map { schedulerDTO ->
            validateScheduler(schedulerDTO)
            schedulerDTO.toScheduler()
        }

        schedulerRepository.saveAll(schedules)
    }

    fun saveSchedulerConfig(schedulerConfigDTO: SchedulerConfigDTO) {
        val config = schedulerConfigDTO.toSchedulerConfig()

        validateDensityRange(config)

        schedulerConfigRepository.save(config)
    }

    private fun validateDensityRange(config: SchedulerConfig) {
        if (config.minScheduleDensity > config.maxScheduleDensity ||
            config.minScheduleDensity == config.maxScheduleDensity) {
            throw BusinessException("Os valores da densidade dos eventos são inválidos.")
        }
    }

    fun saveSchedulerConfigBatch(schedulerConfigDTOList: List<SchedulerConfigDTO>) {
        val configs = schedulerConfigDTOList.map {
            val config = it.toSchedulerConfig()
            validateDensityRange(config)

            config
        }

        schedulerConfigRepository.saveAll(configs)
    }

    fun getSchedulesImport(filter: CommonImportFilter, pageInfos: ImportPageInfos): List<SchedulerDTO> {
        return customSchedulerRepository.getSchedulesImport(filter, pageInfos)
    }

    fun getSchedulerConfigsImport(filter: CommonImportFilter, pageInfos: ImportPageInfos): List<SchedulerConfigDTO> {
        return customSchedulerConfigRepository.getSchedulerConfigImport(filter, pageInfos)
    }

    private fun SchedulerDTO.toScheduler(): Scheduler {
        val scheduler = schedulerRepository.findById(id!!)

        return when {
            id == null -> {
                Scheduler(
                    academyMemberPerson = personRepository.findById(academyMemberPersonId!!).get(),
                    professionalPerson = personRepository.findById(professionalPersonId!!).get(),
                    scheduledDate = scheduledDate,
                    timeStart = timeStart,
                    timeEnd = timeEnd,
                    canceledDate = canceledDate,
                    situation = situation,
                    compromiseType = compromiseType,
                    observation = observation,
                    creationUser = userRepository.findById(creationUserId!!).get(),
                    updateUser = userRepository.findById(updateUserId!!).get()
                )
            }

            scheduler.isPresent -> {
                scheduler.get().copy(
                    academyMemberPerson = personRepository.findById(academyMemberPersonId!!).get(),
                    professionalPerson = personRepository.findById(professionalPersonId!!).get(),
                    scheduledDate = scheduledDate,
                    timeStart = timeStart,
                    timeEnd = timeEnd,
                    canceledDate = canceledDate,
                    situation = situation,
                    compromiseType = compromiseType,
                    observation = observation,
                    updateUser = userRepository.findById(updateUserId!!).get()
                )
            }

            else -> {
                Scheduler(
                    id = id!!,
                    academyMemberPerson = personRepository.findById(academyMemberPersonId!!).get(),
                    professionalPerson = personRepository.findById(professionalPersonId!!).get(),
                    scheduledDate = scheduledDate,
                    timeStart = timeStart,
                    timeEnd = timeEnd,
                    canceledDate = canceledDate,
                    situation = situation,
                    compromiseType = compromiseType,
                    observation = observation,
                    creationUser = userRepository.findById(creationUserId!!).get(),
                    updateUser = userRepository.findById(updateUserId!!).get()
                )
            }
        }
    }

    private fun SchedulerConfigDTO.toSchedulerConfig(): SchedulerConfig {
        val schedulerConfig = schedulerConfigRepository.findById(id!!)

        return when {
            id == null -> {
                SchedulerConfig(
                    active = active,
                    alarm = alarm,
                    notification = notification,
                    minScheduleDensity = minScheduleDensity,
                    maxScheduleDensity = maxScheduleDensity,
                    person = personRepository.findById(personId!!).get(),
                    creationUser = userRepository.findById(creationUserId!!).get(),
                    updateUser = userRepository.findById(updateUserId!!).get()
                )
            }

            schedulerConfig.isPresent -> {
                schedulerConfig.get().copy(
                    active = active,
                    alarm = alarm,
                    notification = notification,
                    minScheduleDensity = minScheduleDensity,
                    maxScheduleDensity = maxScheduleDensity,
                    person = personRepository.findById(personId!!).get(),
                    updateUser = userRepository.findById(updateUserId!!).get()
                )
            }

            else -> {
                SchedulerConfig(
                    id = id!!,
                    active = active,
                    alarm = alarm,
                    notification = notification,
                    minScheduleDensity = minScheduleDensity,
                    maxScheduleDensity = maxScheduleDensity,
                    person = personRepository.findById(personId!!).get(),
                    creationUser = userRepository.findById(creationUserId!!).get(),
                    updateUser = userRepository.findById(updateUserId!!).get()
                )
            }
        }
    }
}