package br.com.fitnesspro.repository.executions

import br.com.fitnesspro.manager.tasks.config.DeleteOldExecutionLogsConfig
import br.com.fitnesspro.models.logs.ExecutionLog
import br.com.fitnesspro.models.logs.ExecutionLogPackage
import br.com.fitnesspro.shared.communication.enums.execution.EnumExecutionType
import br.com.fitnesspro.shared.communication.paging.PageInfos
import br.com.fitnesspro.shared.communication.query.filter.ExecutionLogsFilter
import br.com.fitnesspro.shared.communication.query.filter.ExecutionLogsPackageFilter

interface ICustomExecutionsLogRepository {

    fun getListExecutionLog(filter: ExecutionLogsFilter, pageInfos: PageInfos): List<ExecutionLog>

    fun getCountListExecutionLog(filter: ExecutionLogsFilter): Int

    fun findNotFinishedExecutionLog(
        userEmail: String,
        executionType: EnumExecutionType,
        endPoint: String,
        methodName: String
    ): ExecutionLog?

    fun getListExecutionLogPackage(filter: ExecutionLogsPackageFilter, pageInfos: PageInfos): List<ExecutionLogPackage>

    fun getCountListExecutionLogPackage(filter: ExecutionLogsPackageFilter): Int

    fun getIdsExecutionLogDelete(config: DeleteOldExecutionLogsConfig): List<String>

    fun getIdsExecutionLogPackageDelete(executionLogId: String): List<String>
}