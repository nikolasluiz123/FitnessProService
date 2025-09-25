package br.com.fitnesspro.scheduled.task.service.mappers

import br.com.fitnesspro.models.scheduledtask.ScheduledTask
import br.com.fitnesspro.service.communication.dtos.scheduledtask.ValidatedScheduledTaskDTO
import org.springframework.stereotype.Service

@Service
class ScheduledTaskServiceMapper {

    fun getValidatedScheduledTaskDTO(model: ScheduledTask): ValidatedScheduledTaskDTO {
        return ValidatedScheduledTaskDTO(
            id = model.id,
            name = model.name,
            intervalMillis = model.intervalMillis,
            lastExecutionTime = model.lastExecutionTime,
            active = model.active,
            handlerBeanName = model.handlerBeanName,
            configJson = model.configJson,
            creationDate = model.creationDate,
            updateDate = model.updateDate
        )
    }

    fun getScheduledTask(dto: ValidatedScheduledTaskDTO): ScheduledTask {
        val model = ScheduledTask(
            name = dto.name!!,
            intervalMillis = dto.intervalMillis!!,
            lastExecutionTime = dto.lastExecutionTime,
            active = dto.active!!,
            handlerBeanName = dto.handlerBeanName!!,
            configJson = dto.configJson
        )

        dto.id?.let { model.id = it }

        return model
    }
}