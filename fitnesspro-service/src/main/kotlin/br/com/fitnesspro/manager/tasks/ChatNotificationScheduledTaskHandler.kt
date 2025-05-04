package br.com.fitnesspro.manager.tasks

import br.com.fitnesspro.manager.tasks.common.AbstractScheduledTaskHandler
import br.com.fitnesspro.services.logs.ExecutionsLogService
import br.com.fitnesspro.services.scheduledtask.ScheduledTaskService
import br.com.fitnesspro.services.scheduledtask.tasks.ChatNotificationExecutorService
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