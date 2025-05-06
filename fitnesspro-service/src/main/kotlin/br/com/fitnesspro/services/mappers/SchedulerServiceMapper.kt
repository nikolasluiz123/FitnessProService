package br.com.fitnesspro.services.mappers

import br.com.fitnesspro.models.scheduler.Scheduler
import br.com.fitnesspro.models.scheduler.SchedulerConfig
import br.com.fitnesspro.repository.general.person.IPersonRepository
import br.com.fitnesspro.repository.scheduler.ISchedulerConfigRepository
import br.com.fitnesspro.repository.scheduler.ISchedulerRepository
import br.com.fitnesspro.shared.communication.dtos.scheduler.SchedulerConfigDTO
import br.com.fitnesspro.shared.communication.dtos.scheduler.SchedulerDTO
import org.springframework.stereotype.Service

@Service
class SchedulerServiceMapper(
    private val personRepository: IPersonRepository,
    private val schedulerRepository: ISchedulerRepository,
    private val schedulerConfigRepository: ISchedulerConfigRepository,
) {
    fun getScheduler(dto: SchedulerDTO): Scheduler {
        val scheduler = dto.id?.let { schedulerRepository.findById(it) }

        return when {
            dto.id == null -> {
                Scheduler(
                    academyMemberPerson = personRepository.findById(dto.academyMemberPersonId!!).get(),
                    professionalPerson = personRepository.findById(dto.professionalPersonId!!).get(),
                    scheduledDate = dto.scheduledDate,
                    timeStart = dto.timeStart,
                    timeEnd = dto.timeEnd,
                    canceledDate = dto.canceledDate,
                    cancellationPerson = dto.cancellationPersonId?.let { personRepository.findById(it).get() },
                    situation = dto.situation,
                    compromiseType = dto.compromiseType,
                    observation = dto.observation,
                    active = dto.active,
                )
            }

            scheduler?.isPresent == true -> {
                scheduler.get().copy(
                    academyMemberPerson = personRepository.findById(dto.academyMemberPersonId!!).get(),
                    professionalPerson = personRepository.findById(dto.professionalPersonId!!).get(),
                    scheduledDate = dto.scheduledDate,
                    timeStart = dto.timeStart,
                    timeEnd = dto.timeEnd,
                    canceledDate = dto.canceledDate,
                    cancellationPerson = dto.cancellationPersonId?.let { personRepository.findById(it).get() },
                    situation = dto.situation,
                    compromiseType = dto.compromiseType,
                    observation = dto.observation,
                    active = dto.active,
                )
            }

            else -> {
                Scheduler(
                    id = dto.id!!,
                    academyMemberPerson = personRepository.findById(dto.academyMemberPersonId!!).get(),
                    professionalPerson = personRepository.findById(dto.professionalPersonId!!).get(),
                    scheduledDate = dto.scheduledDate,
                    timeStart = dto.timeStart,
                    timeEnd = dto.timeEnd,
                    canceledDate = dto.canceledDate,
                    cancellationPerson = dto.cancellationPersonId?.let { personRepository.findById(it).get() },
                    situation = dto.situation,
                    compromiseType = dto.compromiseType,
                    observation = dto.observation,
                    active = dto.active,
                )
            }
        }
    }

    fun getSchedulerConfig(dto: SchedulerConfigDTO): SchedulerConfig {
        val schedulerConfig = dto.id?.let { schedulerConfigRepository.findById(it) }

        return when {
            dto.id == null -> {
                SchedulerConfig(
                    active = dto.active,
                    alarm = dto.alarm,
                    notification = dto.notification,
                    minScheduleDensity = dto.minScheduleDensity,
                    maxScheduleDensity = dto.maxScheduleDensity,
                    person = personRepository.findById(dto.personId!!).get(),
                )
            }

            schedulerConfig?.isPresent == true -> {
                schedulerConfig.get().copy(
                    active = dto.active,
                    alarm = dto.alarm,
                    notification = dto.notification,
                    minScheduleDensity = dto.minScheduleDensity,
                    maxScheduleDensity = dto.maxScheduleDensity,
                    person = personRepository.findById(dto.personId!!).get(),
                )
            }

            else -> {
                SchedulerConfig(
                    id = dto.id!!,
                    active = dto.active,
                    alarm = dto.alarm,
                    notification = dto.notification,
                    minScheduleDensity = dto.minScheduleDensity,
                    maxScheduleDensity = dto.maxScheduleDensity,
                    person = personRepository.findById(dto.personId!!).get(),
                )
            }
        }
    }

    fun getSchedulerConfigDTO(model: SchedulerConfig): SchedulerConfigDTO {
        return SchedulerConfigDTO(
            id = model.id,
            creationDate = model.creationDate,
            updateDate = model.updateDate,
            active = model.active,
            alarm = model.alarm,
            notification = model.notification,
            minScheduleDensity = model.minScheduleDensity,
            maxScheduleDensity = model.maxScheduleDensity,
            personId = model.person?.id,
        )
    }

    fun getSchedulerDTO(model: Scheduler): SchedulerDTO {
        return SchedulerDTO(
            id = model.id,
            creationDate = model.creationDate,
            updateDate = model.updateDate,
            academyMemberPersonId = model.academyMemberPerson?.id,
            professionalPersonId = model.professionalPerson?.id,
            scheduledDate = model.scheduledDate,
            timeStart = model.timeStart,
            timeEnd = model.timeEnd,
            canceledDate = model.canceledDate,
            cancellationPersonId = model.cancellationPerson?.id,
            situation = model.situation,
            compromiseType = model.compromiseType,
            observation = model.observation,
            recurrentConfig = null,
            active = model.active,
            type = null
        )
    }
}