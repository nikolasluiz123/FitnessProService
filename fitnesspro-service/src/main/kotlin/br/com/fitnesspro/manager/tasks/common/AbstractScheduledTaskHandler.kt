package br.com.fitnesspro.manager.tasks.common

import br.com.fitnesspro.manager.IScheduledTaskHandler
import br.com.fitnesspro.services.logs.ExecutionsLogService
import br.com.fitnesspro.services.scheduledtask.ScheduledTaskService
import br.com.fitnesspro.services.scheduledtask.tasks.common.IScheduledTaskExecutorService
import org.springframework.transaction.support.TransactionTemplate

abstract class AbstractScheduledTaskHandler<CONFIG>(
    protected val logService: ExecutionsLogService,
    private val transactionTemplate: TransactionTemplate,
    private val executorService: IScheduledTaskExecutorService<CONFIG>,
    private val scheduledTaskService: ScheduledTaskService
): IScheduledTaskHandler<CONFIG> {

    final override fun execute(config: CONFIG?) {
        var pairIds = Pair<String?, String?>(null, null)

        try {
            pairIds = logService.createScheduledTaskStartLog(endPoint = getHandlerIdentifier())

            if (getUseHibernateTransaction()) {
                transactionTemplate.execute { executorService.execute(config, pairIds) }
            } else {
                executorService.execute(config, pairIds)
            }

            logService.updateScheduledTaskLog(
                logId = pairIds.first,
                logPackageId = pairIds.second,
            )

            scheduledTaskService.updateLastExecutionTime(getHandlerIdentifier())
        } catch (ex: Exception) {
            if (pairIds.first != null && pairIds.second != null) {
                logService.updateScheduledTaskLog(
                    logId = pairIds.first!!,
                    logPackageId = pairIds.second!!,
                    exception = ex
                )
            }

            ex.printStackTrace()
        }
    }
}