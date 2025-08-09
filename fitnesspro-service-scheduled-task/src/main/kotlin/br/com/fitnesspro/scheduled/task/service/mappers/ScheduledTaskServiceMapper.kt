package br.com.fitnesspro.scheduled.task.service.mappers

import br.com.fitnesspro.models.scheduledtask.ScheduledTask
import br.com.fitnesspro.shared.communication.dtos.scheduledtask.ScheduledTaskDTO
import org.springframework.stereotype.Service

@Service
class ScheduledTaskServiceMapper {

    fun getScheduledTaskDTO(model: ScheduledTask): ScheduledTaskDTO {
        return ScheduledTaskDTO(
            id = model.id,
            name = model.name,
            intervalMillis = model.intervalMillis,
            lastExecutionTime = model.lastExecutionTime,
            active = model.active,
            handlerBeanName = model.handlerBeanName,
            configJson = model.configJson
        )
    }

    fun getScheduledTask(dto: ScheduledTaskDTO): ScheduledTask {
        val model = br.com.fitnesspro.models.scheduledtask.ScheduledTask(
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