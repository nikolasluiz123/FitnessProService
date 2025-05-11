package br.com.fitnesspro.manager.tasks

import br.com.fitnesspro.manager.tasks.common.AbstractScheduledTaskHandler
import br.com.fitnesspro.services.logs.ExecutionsLogService
import br.com.fitnesspro.services.scheduledtask.ScheduledTaskService
import br.com.fitnesspro.services.scheduledtask.tasks.SchedulerAntecedenceNotificationExecutorService
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