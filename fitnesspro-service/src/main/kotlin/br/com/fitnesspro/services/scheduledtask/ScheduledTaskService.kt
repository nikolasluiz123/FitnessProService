package br.com.fitnesspro.services.scheduledtask

import br.com.fitnesspro.core.extensions.dateTimeNow
import br.com.fitnesspro.exception.BusinessException
import br.com.fitnesspro.manager.ScheduledTaskSavedEvent
import br.com.fitnesspro.models.scheduledtask.ScheduledTask
import br.com.fitnesspro.repository.scheduledtask.ICustomScheduledTaskRepository
import br.com.fitnesspro.repository.scheduledtask.IScheduledTaskRepository
import br.com.fitnesspro.shared.communication.dtos.scheduledtask.ScheduledTaskDTO
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class ScheduledTaskService(
    private val customScheduledTaskRepository: ICustomScheduledTaskRepository,
    private val scheduledTaskRepository: IScheduledTaskRepository,
    private val eventPublisher: ApplicationEventPublisher
) {

    fun saveScheduledTask(dto: ScheduledTaskDTO) {
        validateTask(dto)

        val model = dto.toScheduledTask()
        scheduledTaskRepository.save(model)

        eventPublisher.publishEvent(ScheduledTaskSavedEvent(taskId = model.id, new = dto.id == null))
    }

    private fun validateTask(dto: ScheduledTaskDTO) {
        val anotherTask = scheduledTaskRepository.findByHandlerBeanName(dto.handlerBeanName!!)
        val existsTaskWithEqualsBeanName = anotherTask?.handlerBeanName == dto.handlerBeanName && dto.id != anotherTask?.id

        if (existsTaskWithEqualsBeanName) {
            throw BusinessException("Já existe uma tarefa com o mesmo nome do handler, esse valor deve ser único.")
        }
    }

    fun updateLastExecutionTime(handlerBeanName: String) {
        scheduledTaskRepository.findByHandlerBeanName(handlerBeanName)?.apply {
            lastExecutionTime = dateTimeNow()
            scheduledTaskRepository.save(this)
        }
    }

    fun getListScheduledTask(): List<ScheduledTaskDTO> {
        return customScheduledTaskRepository.getListScheduledTask().map { it.toScheduledTaskDTO() }
    }

    fun getScheduledTaskById(id: String): ScheduledTaskDTO? {
        return scheduledTaskRepository.findById(id).getOrNull()?.toScheduledTaskDTO()
    }

    private fun ScheduledTask.toScheduledTaskDTO(): ScheduledTaskDTO {
        return ScheduledTaskDTO(
            id = id,
            name = name,
            intervalMillis = intervalMillis,
            lastExecutionTime = lastExecutionTime,
            active = active,
            handlerBeanName = handlerBeanName,
            configJson = configJson
        )
    }

    private fun ScheduledTaskDTO.toScheduledTask(): ScheduledTask {
        val model = ScheduledTask(
            name = name!!,
            intervalMillis = intervalMillis!!,
            lastExecutionTime = lastExecutionTime,
            active = active!!,
            handlerBeanName = handlerBeanName!!,
            configJson = configJson
        )

        id?.let { model.id = it }

        return model
    }
}