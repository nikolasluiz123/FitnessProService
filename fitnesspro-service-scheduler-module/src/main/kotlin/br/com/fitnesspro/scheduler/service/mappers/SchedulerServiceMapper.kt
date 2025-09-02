package br.com.fitnesspro.scheduler.service.mappers

import br.com.fitnesspro.authentication.repository.auditable.IPersonRepository
import br.com.fitnesspro.authentication.repository.auditable.ISchedulerConfigRepository
import br.com.fitnesspro.models.scheduler.Scheduler
import br.com.fitnesspro.models.scheduler.SchedulerConfig
import br.com.fitnesspro.scheduler.repository.auditable.ISchedulerRepository
import br.com.fitnesspro.service.communication.dtos.scheduler.ValidatedSchedulerConfigDTO
import br.com.fitnesspro.service.communication.dtos.scheduler.ValidatedSchedulerDTO
import br.com.fitnesspro.shared.communication.dtos.scheduler.interfaces.ISchedulerDTO
import org.springframework.stereotype.Service

@Service
class SchedulerServiceMapper(
    private val personRepository: IPersonRepository,
    private val schedulerRepository: ISchedulerRepository,
    private val schedulerConfigRepository: ISchedulerConfigRepository,
) {
    fun getScheduler(dto: ISchedulerDTO): Scheduler {
        val scheduler = dto.id?.let { schedulerRepository.findById(it) }

        return when {
            dto.id == null -> {
                Scheduler(
                    academyMemberPerson = personRepository.findById(dto.academyMemberPersonId!!).get(),
                    professionalPerson = personRepository.findById(dto.professionalPersonId!!).get(),
                    dateTimeStart = dto.dateTimeStart,
                    dateTimeEnd = dto.dateTimeEnd,
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
                    dateTimeStart = dto.dateTimeStart,
                    dateTimeEnd = dto.dateTimeEnd,
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
                    dateTimeStart = dto.dateTimeStart,
                    dateTimeEnd = dto.dateTimeEnd,
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

    fun getSchedulerConfig(dto: ValidatedSchedulerConfigDTO): SchedulerConfig {
        val schedulerConfig = dto.id?.let { schedulerConfigRepository.findById(it) }

        return when {
            dto.id == null -> {
                SchedulerConfig(
                    notification = dto.notification,
                    notificationAntecedenceTime = dto.notificationAntecedenceTime,
                    minScheduleDensity = dto.minScheduleDensity,
                    maxScheduleDensity = dto.maxScheduleDensity,
                    person = personRepository.findById(dto.personId!!).get(),
                )
            }

            schedulerConfig?.isPresent == true -> {
                schedulerConfig.get().copy(
                    notification = dto.notification,
                    notificationAntecedenceTime = dto.notificationAntecedenceTime,
                    minScheduleDensity = dto.minScheduleDensity,
                    maxScheduleDensity = dto.maxScheduleDensity,
                    person = personRepository.findById(dto.personId!!).get(),
                )
            }

            else -> {
                SchedulerConfig(
                    id = dto.id!!,
                    notification = dto.notification,
                    notificationAntecedenceTime = dto.notificationAntecedenceTime,
                    minScheduleDensity = dto.minScheduleDensity,
                    maxScheduleDensity = dto.maxScheduleDensity,
                    person = personRepository.findById(dto.personId!!).get(),
                )
            }
        }
    }

    fun getSchedulerConfigDTO(model: SchedulerConfig): ValidatedSchedulerConfigDTO {
        return ValidatedSchedulerConfigDTO(
            id = model.id,
            creationDate = model.creationDate,
            updateDate = model.updateDate,
            notification = model.notification,
            notificationAntecedenceTime = model.notificationAntecedenceTime,
            minScheduleDensity = model.minScheduleDensity,
            maxScheduleDensity = model.maxScheduleDensity,
            personId = model.person?.id,
        )
    }

    fun getSchedulerDTO(model: Scheduler): ValidatedSchedulerDTO {
        return ValidatedSchedulerDTO(
            id = model.id,
            creationDate = model.creationDate,
            updateDate = model.updateDate,
            academyMemberPersonId = model.academyMemberPerson?.id,
            professionalPersonId = model.professionalPerson?.id,
            dateTimeStart = model.dateTimeStart,
            dateTimeEnd = model.dateTimeEnd,
            canceledDate = model.canceledDate,
            cancellationPersonId = model.cancellationPerson?.id,
            situation = model.situation,
            compromiseType = model.compromiseType,
            observation = model.observation,
            active = model.active,
            type = null
        )
    }
}