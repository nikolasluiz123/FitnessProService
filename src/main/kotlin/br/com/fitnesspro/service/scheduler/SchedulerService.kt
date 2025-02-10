package br.com.fitnesspro.service.scheduler

import br.com.fitnesspro.dto.scheduler.RecurrentConfigDTO
import br.com.fitnesspro.dto.scheduler.SchedulerConfigDTO
import br.com.fitnesspro.dto.scheduler.SchedulerDTO
import br.com.fitnesspro.enums.EnumDateTimePatterns.*
import br.com.fitnesspro.exception.BusinessException
import br.com.fitnesspro.extensions.dateNow
import br.com.fitnesspro.extensions.format
import br.com.fitnesspro.extensions.timeNow
import br.com.fitnesspro.models.general.Person
import br.com.fitnesspro.models.scheduler.Scheduler
import br.com.fitnesspro.models.scheduler.SchedulerConfig
import br.com.fitnesspro.models.scheduler.enums.EnumSchedulerType
import br.com.fitnesspro.repository.general.academy.ICustomAcademyRepository
import br.com.fitnesspro.repository.general.person.IPersonRepository
import br.com.fitnesspro.repository.scheduler.ICustomSchedulerRepository
import br.com.fitnesspro.repository.scheduler.ISchedulerConfigRepository
import br.com.fitnesspro.repository.scheduler.ISchedulerRepository
import org.springframework.stereotype.Service

@Service
class SchedulerService(
    private val schedulerRepository: ISchedulerRepository,
    private val customSchedulerRepository: ICustomSchedulerRepository,
    private val schedulerConfigRepository: ISchedulerConfigRepository,
    private val personRepository: IPersonRepository,
    private val customAcademyRepository: ICustomAcademyRepository
) {
    fun saveScheduler(schedulerDTO: SchedulerDTO) {
        validateScheduler(schedulerDTO)

        when (schedulerDTO.type!!) {
            EnumSchedulerType.SUGGESTION, EnumSchedulerType.UNIQUE -> {
                val scheduler = schedulerDTO.toScheduler()
                schedulerRepository.save(scheduler)
            }

            EnumSchedulerType.RECURRENT -> {
                val config = schedulerDTO.recurrentConfig

                val scheduleDates = generateSequence(config.dateStart) { it.plusDays(1) }
                    .takeWhile { it <= config.dateEnd }
                    .filter { config.dayWeeks.contains(it.dayOfWeek) }
                    .toList()

                val schedules = scheduleDates.map { date ->
                    schedulerDTO.toScheduler().copy(scheduledDate = date)
                }

                validateConflictRecurrent(schedules)

                schedulerRepository.saveAll(schedules)
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
                validateDateConfigPeriod(dto.recurrentConfig)
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

    private fun SchedulerDTO.toScheduler(): Scheduler {
        return id?.let { schedulerId ->
            schedulerRepository.findById(schedulerId).get().copy(
                academyMemberPerson = personRepository.findById(academyMemberPersonId!!).get(),
                professionalPerson = personRepository.findById(professionalPersonId!!).get(),
                scheduledDate = scheduledDate,
                timeStart = timeStart,
                timeEnd = timeEnd,
                canceledDate = canceledDate,
                situation = situation,
                compromiseType = compromiseType,
                observation = observation
            )
        } ?: Scheduler(
            academyMemberPerson = personRepository.findById(academyMemberPersonId!!).get(),
            professionalPerson = personRepository.findById(professionalPersonId!!).get(),
            scheduledDate = scheduledDate,
            timeStart = timeStart,
            timeEnd = timeEnd,
            canceledDate = canceledDate,
            situation = situation,
            compromiseType = compromiseType,
            observation = observation
        )
    }

    private fun SchedulerConfigDTO.toSchedulerConfig(): SchedulerConfig {
        return id?.let {
            schedulerConfigRepository.findById(it).get().copy(
                active = active,
                alarm = alarm,
                notification = notification,
                minScheduleDensity = minScheduleDensity,
                maxScheduleDensity = maxScheduleDensity
            )
        } ?: SchedulerConfig(
            active = active,
            alarm = alarm,
            notification = notification,
            minScheduleDensity = minScheduleDensity,
            maxScheduleDensity = maxScheduleDensity
        )
    }
}