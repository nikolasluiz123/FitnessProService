package br.com.fitnesspro.common.service.scheduledtasks

import br.com.fitnesspro.common.repository.jpa.logs.ICustomDeleteOldExecutionLogsRepository
import br.com.fitnesspro.common.scheduledtasks.config.DeleteOldExecutionLogsConfig
import br.com.fitnesspro.log.repository.jpa.logpackage.IExecutionsLogPackageRepository
import br.com.fitnesspro.log.repository.jpa.IExecutionsLogRepository
import br.com.fitnesspro.log.service.ExecutionsLogService
import br.com.fitnesspro.models.logs.ExecutionLog
import br.com.fitnesspro.models.logs.ExecutionLogPackage
import br.com.fitnesspro.scheduled.task.service.IScheduledTaskExecutorService
import org.springframework.stereotype.Service
import java.util.*

@Service
class DeleteOldExecutionLogsExecutorService(
    private val customDeleteExecutionsLogRepository: ICustomDeleteOldExecutionLogsRepository,
    private val executionsLogRepository: IExecutionsLogRepository,
    private val executionLogsPackageRepository: IExecutionsLogPackageRepository,
    private val logService: ExecutionsLogService
): IScheduledTaskExecutorService<DeleteOldExecutionLogsConfig> {

    override fun execute(config: DeleteOldExecutionLogsConfig?, pairIds: Pair<String, String>) {
        val executionLogs = customDeleteExecutionsLogRepository.getIdsExecutionLogDelete(config!!)
        val executionLogsPackages = executionLogs.map {
            customDeleteExecutionsLogRepository.getIdsExecutionLogPackageDelete(it)
        }.flatten()

        executionLogsPackages.chunked(60000).forEach(executionLogsPackageRepository::deleteAllByIdInBatch)
        executionLogs.chunked(60000).forEach(executionsLogRepository::deleteAllByIdInBatch)

        val additionalInformation = StringJoiner("\n").apply {
            add(" ------------------------------------ Logs Deletados ------------------------------------ ")
            add(" ${executionLogs.size} registros de ${ExecutionLog::class.simpleName} ")
            add(" ${executionLogsPackages.size} registros de ${ExecutionLogPackage::class.simpleName} ")
        }

        logService.updateScheduledTaskLogWithAdditionalInfos(pairIds.second, additionalInformation)
    }

}