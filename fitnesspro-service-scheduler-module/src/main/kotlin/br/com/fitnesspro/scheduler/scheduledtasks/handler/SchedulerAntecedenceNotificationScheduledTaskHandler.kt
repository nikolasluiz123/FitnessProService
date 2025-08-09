package br.com.fitnesspro.scheduler.scheduledtasks.handler

import br.com.fitnesspro.log.service.ExecutionsLogService
import br.com.fitnesspro.scheduler.service.scheduledtasks.SchedulerAntecedenceNotificationExecutorService
import br.com.fitnesspro.scheduled.task.handler.AbstractScheduledTaskHandler
import br.com.fitnesspro.scheduled.task.service.ScheduledTaskService
import org.springframework.stereotype.Component
import org.springframework.transaction.support.TransactionTemplate

internal const val SCHEDULER_ANTECEDENCE_NOTIFICATION_SCHEDULED = "schedulerAntecedenceNotificationScheduled"

@Component(SCHEDULER_ANTECEDENCE_NOTIFICATION_SCHEDULED)
class SchedulerAntecedenceNotificationScheduledTaskHandler(
    logService: ExecutionsLogService,
    transactionTemplate: TransactionTemplate,
    executorService: SchedulerAntecedenceNotificationExecutorService,
    scheduledTaskService: ScheduledTaskService
) : AbstractScheduledTaskHandler<Nothing?>(logService, transactionTemplate, executorService, scheduledTaskService) {

    override fun configClass() = null

    override fun getHandlerIdentifier(): String = SCHEDULER_ANTECEDENCE_NOTIFICATION_SCHEDULED

}