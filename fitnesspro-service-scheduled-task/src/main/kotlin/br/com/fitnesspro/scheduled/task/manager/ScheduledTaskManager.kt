package br.com.fitnesspro.scheduled.task.manager

import br.com.fitnesspro.core.gson.defaultGSon
import br.com.fitnesspro.scheduled.task.handler.IScheduledTaskHandler
import br.com.fitnesspro.scheduled.task.service.ScheduledTaskService
import br.com.fitnesspro.service.communication.dtos.scheduledtask.ValidatedScheduledTaskDTO
import com.google.gson.GsonBuilder
import org.springframework.context.event.EventListener
import org.springframework.scheduling.TaskScheduler
import org.springframework.stereotype.Component
import java.time.Duration
import java.util.concurrent.ScheduledFuture

@Component
class ScheduledTaskManager(
    private val taskScheduler: TaskScheduler,
    private val handlers: Map<String, IScheduledTaskHandler<*>>,
    private val taskService: ScheduledTaskService
) {
    private val scheduledTasks = mutableMapOf<String, ScheduledFuture<*>>()

    fun startAllTasks() {
        taskService.getListScheduledTask().forEach(::scheduleTask)
    }

    @EventListener
    fun onScheduledTaskCreated(event: ScheduledTaskSavedEvent) {
        val task = taskService.getScheduledTaskById(event.taskId) ?: throw IllegalStateException("Task '${event.taskId}' não encontrada")

        if (task.active!!) {
            if (event.new) {
                scheduleTask(task)
            } else {
                stopTask(event.taskId)
                scheduleTask(task)
            }
        } else {
            stopTask(event.taskId)
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun scheduleTask(task: ValidatedScheduledTaskDTO) {
        val handler = handlers[task.handlerBeanName]
        val typedHandler = handler as IScheduledTaskHandler<Any>
        val config = if (task.configJson != null && handler.configClass() != null) {
            GsonBuilder().defaultGSon().fromJson(task.configJson, handler.configClass())
        } else {
            null
        }

        val duration = Duration.ofMillis(task.intervalMillis!!)
        
        val future = taskScheduler.scheduleAtFixedRate(
            { typedHandler.execute(config) },
            duration
        )

        scheduledTasks[task.id!!] = future
    }

    fun stopTask(taskId: String) {
        scheduledTasks[taskId]?.cancel(false)
        scheduledTasks.remove(taskId)
    }

}