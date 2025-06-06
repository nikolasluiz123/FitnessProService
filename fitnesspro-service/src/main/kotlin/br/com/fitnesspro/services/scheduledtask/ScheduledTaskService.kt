package br.com.fitnesspro.services.scheduledtask

import br.com.fitnesspro.core.extensions.dateTimeNow
import br.com.fitnesspro.exception.BusinessException
import br.com.fitnesspro.manager.ScheduledTaskSavedEvent
import br.com.fitnesspro.repository.scheduledtask.ICustomScheduledTaskRepository
import br.com.fitnesspro.repository.scheduledtask.IScheduledTaskRepository
import br.com.fitnesspro.services.mappers.ScheduledTaskServiceMapper
import br.com.fitnesspro.shared.communication.dtos.scheduledtask.ScheduledTaskDTO
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.MessageSource
import org.springframework.stereotype.Service
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Service
class ScheduledTaskService(
    private val customScheduledTaskRepository: ICustomScheduledTaskRepository,
    private val scheduledTaskRepository: IScheduledTaskRepository,
    private val eventPublisher: ApplicationEventPublisher,
    private val scheduledTaskServiceMapper: ScheduledTaskServiceMapper,
    private val messageSource: MessageSource
) {

    fun saveScheduledTask(dto: ScheduledTaskDTO) {
        validateTask(dto)

        val model = scheduledTaskServiceMapper.getScheduledTask(dto)
        scheduledTaskRepository.save(model)

        eventPublisher.publishEvent(ScheduledTaskSavedEvent(taskId = model.id, new = dto.id == null))
    }

    private fun validateTask(dto: ScheduledTaskDTO) {
        val anotherTask = scheduledTaskRepository.findByHandlerBeanName(dto.handlerBeanName!!)
        val existsTaskWithEqualsBeanName = anotherTask?.handlerBeanName == dto.handlerBeanName && dto.id != anotherTask?.id

        if (existsTaskWithEqualsBeanName) {
            val message = messageSource.getMessage("scheduled.task.error.handler.exists", null, Locale.getDefault())
            throw BusinessException(message)
        }
    }

    fun updateLastExecutionTime(handlerBeanName: String) {
        scheduledTaskRepository.findByHandlerBeanName(handlerBeanName)?.apply {
            lastExecutionTime = dateTimeNow()
            scheduledTaskRepository.save(this)
        }
    }

    fun getListScheduledTask(): List<ScheduledTaskDTO> {
        return customScheduledTaskRepository.getListScheduledTask().map(scheduledTaskServiceMapper::getScheduledTaskDTO)
    }

    fun getScheduledTaskById(id: String): ScheduledTaskDTO? {
        return scheduledTaskRepository.findById(id).getOrNull()?.let(scheduledTaskServiceMapper::getScheduledTaskDTO)
    }

}