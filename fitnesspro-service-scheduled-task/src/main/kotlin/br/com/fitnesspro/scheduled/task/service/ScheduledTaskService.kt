package br.com.fitnesspro.scheduled.task.service

import br.com.fitnesspro.core.exceptions.BusinessException
import br.com.fitnesspro.core.extensions.dateTimeNow
import br.com.fitnesspro.scheduled.task.manager.ScheduledTaskSavedEvent
import br.com.fitnesspro.scheduled.task.repository.auditable.IScheduledTaskRepository
import br.com.fitnesspro.scheduled.task.repository.jpa.ICustomScheduledTaskRepository
import br.com.fitnesspro.scheduled.task.service.mappers.ScheduledTaskServiceMapper
import br.com.fitnesspro.service.communication.dtos.scheduledtask.ValidatedScheduledTaskDTO
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

    fun saveScheduledTask(dto: ValidatedScheduledTaskDTO) {
        validateTask(dto)

        val model = scheduledTaskServiceMapper.getScheduledTask(dto)
        scheduledTaskRepository.save(model)

        eventPublisher.publishEvent(ScheduledTaskSavedEvent(taskId = model.id, new = dto.id == null))
    }

    private fun validateTask(dto: ValidatedScheduledTaskDTO) {
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

    fun getListScheduledTask(): List<ValidatedScheduledTaskDTO> {
        return customScheduledTaskRepository.getListScheduledTask().map(scheduledTaskServiceMapper::getValidatedScheduledTaskDTO)
    }

    fun getScheduledTaskById(id: String): ValidatedScheduledTaskDTO? {
        return scheduledTaskRepository.findById(id).getOrNull()?.let(scheduledTaskServiceMapper::getValidatedScheduledTaskDTO)
    }

}