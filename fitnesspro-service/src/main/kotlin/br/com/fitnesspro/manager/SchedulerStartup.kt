package br.com.fitnesspro.manager

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