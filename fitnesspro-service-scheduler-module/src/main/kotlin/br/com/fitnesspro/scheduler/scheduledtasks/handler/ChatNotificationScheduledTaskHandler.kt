package br.com.fitnesspro.scheduler.scheduledtasks.handler

import br.com.fitnesspro.log.service.ExecutionsLogService
import br.com.fitnesspro.scheduler.service.scheduledtasks.ChatNotificationExecutorService
import br.com.fitnesspro.scheduled.task.handler.AbstractScheduledTaskHandler
import br.com.fitnesspro.scheduled.task.service.ScheduledTaskService
import org.springframework.stereotype.Component
import org.springframework.transaction.support.TransactionTemplate

internal const val CHAT_NOTIFICATION = "chatNotification"

@Component(CHAT_NOTIFICATION)
class ChatNotificationScheduledTaskHandler(
    logService: ExecutionsLogService,
    transactionTemplate: TransactionTemplate,
    executorService: ChatNotificationExecutorService,
    scheduledTaskService: ScheduledTaskService
) : AbstractScheduledTaskHandler<Nothing>(logService, transactionTemplate, executorService, scheduledTaskService) {

    override fun configClass() = null

    override fun getHandlerIdentifier(): String = CHAT_NOTIFICATION

    override fun getUseHibernateTransaction(): Boolean = false

}