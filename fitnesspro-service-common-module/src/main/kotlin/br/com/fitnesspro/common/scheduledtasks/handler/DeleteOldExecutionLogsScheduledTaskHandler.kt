package br.com.fitnesspro.common.scheduledtasks.handler

import br.com.fitnesspro.common.scheduledtasks.config.DeleteOldExecutionLogsConfig
import br.com.fitnesspro.common.service.scheduledtasks.DeleteOldExecutionLogsExecutorService
import br.com.fitnesspro.log.service.ExecutionsLogService
import br.com.fitnesspro.scheduled.task.handler.AbstractScheduledTaskHandler
import br.com.fitnesspro.scheduled.task.service.ScheduledTaskService
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