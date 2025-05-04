package br.com.fitnesspro.services.scheduledtask.tasks

import br.com.fitnesspro.manager.tasks.config.DeleteOldExecutionLogsConfig
import br.com.fitnesspro.models.logs.ExecutionLog
import br.com.fitnesspro.models.logs.ExecutionLogPackage
import br.com.fitnesspro.repository.executions.ICustomExecutionsLogRepository
import br.com.fitnesspro.repository.executions.IExecutionsLogPackageRepository
import br.com.fitnesspro.repository.executions.IExecutionsLogRepository
import br.com.fitnesspro.services.logs.ExecutionsLogService
import br.com.fitnesspro.services.scheduledtask.tasks.common.IScheduledTaskExecutorService
import org.springframework.stereotype.Service
import java.util.*

@Service
class DeleteOldExecutionLogsExecutorService(
    private val customExecutionsLogRepository: ICustomExecutionsLogRepository,
    private val executionsLogRepository: IExecutionsLogRepository,
    private val executionLogsPackageRepository: IExecutionsLogPackageRepository,
    private val logService: ExecutionsLogService
): IScheduledTaskExecutorService<DeleteOldExecutionLogsConfig> {

    override fun execute(config: DeleteOldExecutionLogsConfig?, pairIds: Pair<String, String>) {
        val executionLogs = customExecutionsLogRepository.getIdsExecutionLogDelete(config!!)
        val executionLogsPackages = executionLogs.map {
            customExecutionsLogRepository.getIdsExecutionLogPackageDelete(it)
        }.flatten()

        executionLogsPackageRepository.deleteAllByIdInBatch(executionLogsPackages)
        executionsLogRepository.deleteAllByIdInBatch(executionLogs)

        val additionalInformation = StringJoiner("\n").apply {
            add(" ------------------------------------ Logs Deletados ------------------------------------ ")
            add(" ${executionLogs.size} registros de ${ExecutionLog::class.simpleName} ")
            add(" ${executionLogsPackages.size} registros de ${ExecutionLogPackage::class.simpleName} ")
        }

        logService.updateScheduledTaskLogWithAdditionalInfos(pairIds.second, additionalInformation)
    }

}