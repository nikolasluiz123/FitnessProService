package br.com.fitnesspro.repository.executions

import br.com.fitnesspro.core.extensions.dateTimeNow
import br.com.fitnesspro.manager.tasks.config.DeleteOldExecutionLogsConfig
import br.com.fitnesspro.models.logs.ExecutionLog
import br.com.fitnesspro.models.logs.ExecutionLogPackage
import br.com.fitnesspro.repository.common.helper.Constants.QR_NL
import br.com.fitnesspro.repository.common.query.Parameter
import br.com.fitnesspro.repository.common.query.setParameters
import br.com.fitnesspro.shared.communication.enums.execution.EnumExecutionState.PENDING
import br.com.fitnesspro.shared.communication.enums.execution.EnumExecutionState.RUNNING
import br.com.fitnesspro.shared.communication.enums.execution.EnumExecutionType
import br.com.fitnesspro.shared.communication.paging.PageInfos
import br.com.fitnesspro.shared.communication.query.filter.ExecutionLogsFilter
import br.com.fitnesspro.shared.communication.query.filter.ExecutionLogsPackageFilter
import jakarta.persistence.EntityManager
import jakarta.persistence.NoResultException
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class CustomExecutionsLogRepositoryImpl: ICustomExecutionsLogRepository {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    override fun getListExecutionLog(filter: ExecutionLogsFilter, pageInfos: PageInfos): List<ExecutionLog> {
        val queryParams = mutableListOf<Parameter>()

        val select = StringJoiner(QR_NL).apply {
            add(" select log ")
        }

        val from = getFromListExecutionLog()

        val where = getWhereListExecutionLog(filter, queryParams)

        val orderBy = StringJoiner(QR_NL).apply {
            add(" order by log.creationDate desc ")
        }

        val sql = StringJoiner(QR_NL).apply {
            add(select.toString())
            add(from.toString())
            add(where.toString())
            add(orderBy.toString())
        }

        val query = entityManager.createQuery(sql.toString(), ExecutionLog::class.java)
        query.setParameters(queryParams)

        query.firstResult = pageInfos.pageSize * pageInfos.pageNumber
        query.maxResults = pageInfos.pageSize

        return query.resultList
    }

    private fun getFromListExecutionLog(): StringJoiner {
        return StringJoiner(QR_NL).apply {
            add(" from ${ExecutionLog::class.java.name} log ")
            add(" left join log.user user ")
            add(" left join log.device device ")
            add(" left join log.application application ")
        }
    }

    private fun getWhereListExecutionLog(
        filter: ExecutionLogsFilter,
        queryParams: MutableList<Parameter>
    ): StringJoiner {
        return StringJoiner(QR_NL).apply {
            add(" where 1 = 1 ")

            filter.creationDate?.let {
                add(" and log.creationDate between :creationDateStart and :creationDateEnd ")
                queryParams.add(Parameter(name = "creationDateStart", value = it.first))
                queryParams.add(Parameter(name = "creationDateEnd", value = it.second))
            }

            filter.type?.let {
                add(" and log.type = :type ")
                queryParams.add(Parameter(name = "type", value = it))
            }

            filter.state?.let {
                add(" and log.state = :state ")
                queryParams.add(Parameter(name = "state", value = it))
            }

            filter.endPoint?.let {
                add(" and log.endPoint like :endPoint ")
                queryParams.add(Parameter(name = "endPoint", value = "%$it%"))
            }

            filter.methodName?.let {
                add(" and lower(log.methodName) like lower(:methodName) ")
                queryParams.add(Parameter(name = "methodName", value = "%$it%"))
            }

            filter.userEmail?.let {
                add(" and lower(user.email) like lower(:userEmail) ")
                queryParams.add(Parameter(name = "userEmail", value = "%$it%"))
            }

            filter.deviceId?.let {
                add(" and device.id like :deviceId ")
                queryParams.add(Parameter(name = "deviceId", value = "%$it%"))
            }

            filter.applicationName?.let {
                add(" and lower(application.name) like lower(:applicationName) ")
                queryParams.add(Parameter(name = "applicationName", value = "%$it%"))
            }
        }
    }

    override fun getCountListExecutionLog(filter: ExecutionLogsFilter): Int {
        val queryParams = mutableListOf<Parameter>()

        val select = StringJoiner(QR_NL).apply {
            add(" select count(log.id) ")
        }

        val from = getFromListExecutionLog()

        val where = getWhereListExecutionLog(filter, queryParams)

        val sql = StringJoiner(QR_NL).apply {
            add(select.toString())
            add(from.toString())
            add(where.toString())
        }

        val query = entityManager.createQuery(sql.toString(), Long::class.java)
        query.setParameters(queryParams)

        return query.singleResult.toInt()
    }

    override fun findNotFinishedExecutionLog(
        userEmail: String,
        executionType: EnumExecutionType,
        endPoint: String,
        methodName: String
    ): ExecutionLog? {
        val queryParams = mutableListOf(
            Parameter("pNotFinishedStates", listOf(PENDING.name, RUNNING.name)),
            Parameter("pUserEmail", userEmail),
            Parameter("pExecutionType", executionType),
            Parameter("pEndPoint", endPoint),
            Parameter("pMethodName", methodName)
        )

        val select = StringJoiner(QR_NL).apply {
            add(" select e ")
        }

        val from = StringJoiner(QR_NL).apply {
            add(" from ${ExecutionLog::class.java.name} e ")
            add(" inner join e.user user ")
        }

        val where = StringJoiner(QR_NL).apply {
            add(" where e.state in (:pNotFinishedStates) ")
            add(" and user.email = :pUserEmail ")
            add(" and e.type = :pExecutionType ")
            add(" and e.endPoint = :pEndPoint ")
            add(" and e.methodName = :pMethodName ")
        }

        val sql = StringJoiner(QR_NL).apply {
            add(select.toString())
            add(from.toString())
            add(where.toString())
        }

        val query = entityManager.createQuery(sql.toString(), ExecutionLog::class.java)
        query.setParameters(queryParams)

        return try {
            query.singleResult
        } catch (e: NoResultException) {
            null
        }
    }

    override fun getListExecutionLogPackage(
        filter: ExecutionLogsPackageFilter,
        pageInfos: PageInfos
    ): List<ExecutionLogPackage> {
        val queryParams = mutableListOf<Parameter>()

        val select = StringJoiner(QR_NL).apply {
            add(" select lp ")
        }

        val from = getFromListExecutionLogPackage()

        val where = getWhereListExecutionLogPackage(filter, queryParams)

        val orderBy = StringJoiner(QR_NL).apply {
            if (filter.sort == null) {
                add(" order by lp.service_execution_start desc ")
            } else {
                val sortField = filter.sort?.field?.fieldName
                val order = if (filter.sort?.asc!!) "asc" else "desc"

                add(" order by lp.$sortField $order ")
            }
        }

        val sql = StringJoiner(QR_NL).apply {
            add(select.toString())
            add(from.toString())
            add(where.toString())
            add(orderBy.toString())
        }

        val query = entityManager.createQuery(sql.toString(), ExecutionLogPackage::class.java)
        query.setParameters(queryParams)

        query.firstResult = pageInfos.pageSize * pageInfos.pageNumber
        query.maxResults = pageInfos.pageSize

        return query.resultList
    }

    private fun getWhereListExecutionLogPackage(
        filter: ExecutionLogsPackageFilter,
        queryParams: MutableList<Parameter>
    ): StringJoiner {
        val where = StringJoiner(QR_NL).apply {
            add(" where lp.executionLog.id = :pExecutionLogId ")

            queryParams.add(Parameter(name = "pExecutionLogId", value = filter.executionLogId!!))

            filter.serviceExecutionStart?.let {
                add(" and lp.serviceExecutionStart between :startServiceExecutionStart and :endServiceExecutionStart ")
                queryParams.add(Parameter(name = "startServiceExecutionStart", value = it.first))
                queryParams.add(Parameter(name = "endServiceExecutionStart", value = it.second))
            }

            filter.serviceExecutionEnd?.let {
                add(" and lp.serviceExecutionEnd between :startServiceExecutionEnd and :endServiceExecutionEnd ")
                queryParams.add(Parameter(name = "startServiceExecutionEnd", value = it.first))
                queryParams.add(Parameter(name = "endServiceExecutionEnd", value = it.second))
            }

            filter.clientExecutionStart?.let {
                add(" and lp.clientExecutionStart between :startClientExecutionStart and :endClientExecutionStart ")
                queryParams.add(Parameter(name = "startClientExecutionStart", value = it.first))
                queryParams.add(Parameter(name = "endClientExecutionStart", value = it.second))
            }

            filter.clientExecutionEnd?.let {
                add(" and lp.clientExecutionEnd between :startClientExecutionEnd and :endClientExecutionEnd ")
                queryParams.add(Parameter(name = "startClientExecutionEnd", value = it.first))
                queryParams.add(Parameter(name = "endClientExecutionEnd", value = it.second))
            }
        }
        return where
    }

    private fun getFromListExecutionLogPackage(): StringJoiner {
        val from = StringJoiner(QR_NL).apply {
            add(" from ${ExecutionLogPackage::class.java.name} lp ")
        }
        return from
    }

    override fun getCountListExecutionLogPackage(filter: ExecutionLogsPackageFilter): Int {
        val queryParams = mutableListOf<Parameter>()

        val select = StringJoiner(QR_NL).apply {
            add(" select count(lp.id) ")
        }

        val from = getFromListExecutionLogPackage()

        val where = getWhereListExecutionLogPackage(filter, queryParams)

        val sql = StringJoiner(QR_NL).apply {
            add(select.toString())
            add(from.toString())
            add(where.toString())
        }

        val query = entityManager.createQuery(sql.toString(), Long::class.java)
        query.setParameters(queryParams)

        return query.singleResult.toInt()
    }

    override fun getIdsExecutionLogDelete(config: DeleteOldExecutionLogsConfig): List<String> {
        val queryParams = mutableListOf<Parameter>()

        val select = StringJoiner(QR_NL).apply {
            add(" select log.id as id ")
        }

        val from = StringJoiner(QR_NL).apply {
            add(" from ${ExecutionLog::class.java.name} log ")
        }

        val where = StringJoiner(QR_NL).apply {
            add(" where log.creationDate <= :pExclusionDate ")
            queryParams.add(Parameter(name = "pExclusionDate", value = dateTimeNow().minusDays(config.logLifeTimeDays)))
        }

        val sql = StringJoiner(QR_NL).apply {
            add(select.toString())
            add(from.toString())
            add(where.toString())
        }

        val query = entityManager.createQuery(sql.toString(), String::class.java)
        query.setParameters(queryParams)

        return query.resultList
    }

    override fun getIdsExecutionLogPackageDelete(executionLogId: String): List<String> {
        val queryParams = mutableListOf<Parameter>()

        val select = StringJoiner(QR_NL).apply {
            add(" select logPackage.id as id ")
        }

        val from = StringJoiner(QR_NL).apply {
            add(" from ${ExecutionLogPackage::class.java.name} logPackage ")
        }

        val where = StringJoiner(QR_NL).apply {
            add(" where logPackage.executionLog.id = :pLogId ")
            queryParams.add(Parameter(name = "pLogId", value = executionLogId))
        }

        val sql = StringJoiner(QR_NL).apply {
            add(select.toString())
            add(from.toString())
            add(where.toString())
        }

        val query = entityManager.createQuery(sql.toString(), String::class.java)
        query.setParameters(queryParams)

        return query.resultList
    }
}