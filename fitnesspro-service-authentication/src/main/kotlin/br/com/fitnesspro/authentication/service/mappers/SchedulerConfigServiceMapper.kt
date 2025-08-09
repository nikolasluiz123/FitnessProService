package br.com.fitnesspro.authentication.service.mappers

import br.com.fitnesspro.authentication.repository.auditable.IPersonRepository
import br.com.fitnesspro.authentication.repository.auditable.ISchedulerConfigRepository
import br.com.fitnesspro.models.scheduler.SchedulerConfig
import br.com.fitnesspro.shared.communication.dtos.scheduler.SchedulerConfigDTO
import org.springframework.stereotype.Service

@Service
class SchedulerConfigServiceMapper(
    private val personRepository: IPersonRepository,
    private val schedulerConfigRepository: ISchedulerConfigRepository,
) {
    fun getSchedulerConfig(dto: SchedulerConfigDTO): SchedulerConfig {
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

    fun getSchedulerConfigDTO(model: SchedulerConfig): SchedulerConfigDTO {
        return SchedulerConfigDTO(
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
}