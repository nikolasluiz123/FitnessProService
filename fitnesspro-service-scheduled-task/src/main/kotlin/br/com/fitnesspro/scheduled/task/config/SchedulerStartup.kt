package br.com.fitnesspro.scheduled.task.config

import br.com.fitnesspro.scheduled.task.manager.ScheduledTaskManager
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class SchedulerStartup(
    private val manager: ScheduledTaskManager
) {
    @EventListener(ApplicationReadyEvent::class)
    fun onStartup() {
        manager.startAllTasks()
    }
}