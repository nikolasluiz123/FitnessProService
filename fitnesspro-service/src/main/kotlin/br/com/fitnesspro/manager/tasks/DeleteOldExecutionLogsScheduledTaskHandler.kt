package br.com.fitnesspro.manager.tasks

import br.com.fitnesspro.manager.tasks.common.AbstractScheduledTaskHandler
import br.com.fitnesspro.manager.tasks.config.DeleteOldExecutionLogsConfig
import br.com.fitnesspro.services.logs.ExecutionsLogService
import br.com.fitnesspro.services.scheduledtask.ScheduledTaskService
import br.com.fitnesspro.services.scheduledtask.tasks.DeleteOldExecutionLogsExecutorService
import org.springframework.stereotype.Component
import org.springframework.transaction.support.TransactionTemplate

internal const val DELETE_OLD_EXECUTION_LOGS = "deleteOldExecutionLogs"

@Component(DELETE_OLD_EXECUTION_LOGS)
class DeleteOldExecutionLogsScheduledTaskHandler(
    logService: ExecutionsLogService,
    transactionTemplate: TransactionTemplate,
    executorService: DeleteOldExecutionLogsExecutorService,
    scheduledTaskService: ScheduledTaskService
) : AbstractScheduledTaskHandler<DeleteOldExecutionLogsConfig>(logService, transactionTemplate, executorService, scheduledTaskService) {

    override fun configClass() = DeleteOldExecutionLogsConfig::class.java

    override fun getHandlerIdentifier(): String = DELETE_OLD_EXECUTION_LOGS

}