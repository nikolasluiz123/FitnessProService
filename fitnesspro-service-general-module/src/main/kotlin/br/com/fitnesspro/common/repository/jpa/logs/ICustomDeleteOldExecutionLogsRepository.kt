package br.com.fitnesspro.common.repository.jpa.logs

import br.com.fitnesspro.common.scheduledtasks.config.DeleteOldExecutionLogsConfig

interface ICustomDeleteOldExecutionLogsRepository {

    fun getIdsExecutionLogDelete(config: DeleteOldExecutionLogsConfig): List<String>

    fun getIdsExecutionLogPackageDelete(executionLogId: String): List<String>
}