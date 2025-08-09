package br.com.fitnesspro.scheduler.scheduledtasks.handler

import br.com.fitnesspro.log.service.ExecutionsLogService
import br.com.fitnesspro.scheduler.scheduledtasks.config.DeleteOldChatMessagesConfig
import br.com.fitnesspro.scheduler.service.scheduledtasks.DeleteOldChatMessagesExecutorService
import br.com.fitnesspro.scheduled.task.handler.AbstractScheduledTaskHandler
import br.com.fitnesspro.scheduled.task.service.ScheduledTaskService
import org.springframework.stereotype.Component
import org.springframework.transaction.support.TransactionTemplate

internal const val DELETE_OLD_CHAT_MESSAGES = "deleteOldChatMessages"

@Component(DELETE_OLD_CHAT_MESSAGES)
class DeleteOldChatMessagesScheduledTaskHandler(
    logService: ExecutionsLogService,
    transactionTemplate: TransactionTemplate,
    executorService: DeleteOldChatMessagesExecutorService,
    scheduledTaskService: ScheduledTaskService
) : AbstractScheduledTaskHandler<DeleteOldChatMessagesConfig>(logService, transactionTemplate, executorService, scheduledTaskService) {

    override fun configClass() = DeleteOldChatMessagesConfig::class.java

    override fun getHandlerIdentifier(): String = DELETE_OLD_CHAT_MESSAGES

    override fun getUseHibernateTransaction(): Boolean = false

}