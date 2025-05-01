package br.com.fitnesspro.manager.tasks

import br.com.fitnesspro.manager.tasks.common.AbstractScheduledTaskHandler
import br.com.fitnesspro.manager.tasks.config.DeleteOldChatMessagesConfig
import br.com.fitnesspro.services.logs.ExecutionsLogService
import br.com.fitnesspro.services.scheduledtask.ScheduledTaskService
import br.com.fitnesspro.services.scheduledtask.tasks.DeleteOldChatMessagesExecutorService
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