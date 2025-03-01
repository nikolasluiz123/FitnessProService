package br.com.fitnesspro.service.repository.executions

import br.com.fitnesspro.service.models.executions.ExecutionLog
import br.com.fitnesspro.shared.communication.filter.ExecutionLogsFilter
import br.com.fitnesspro.shared.communication.paging.PageInfos

interface ICustomExecutionsLogRepository {

    fun getListExecutionLog(filter: ExecutionLogsFilter,  pageInfos: PageInfos): List<ExecutionLog>

    fun getCountListExecutionLog(filter: ExecutionLogsFilter): Int
}