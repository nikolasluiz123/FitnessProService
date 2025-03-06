package br.com.fitnesspro.service.repository.executions

import br.com.fitnesspro.service.models.executions.ExecutionLog
import br.com.fitnesspro.service.repository.common.helper.Constants.QR_NL
import br.com.fitnesspro.service.repository.common.query.Parameter
import br.com.fitnesspro.service.repository.common.query.setParameters
import br.com.fitnesspro.shared.communication.filter.ExecutionLogsFilter
import br.com.fitnesspro.shared.communication.paging.PageInfos
import jakarta.persistence.EntityManager
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
            val sortField = filter.sort?.field!!
            val order = if (filter.sort?.asc!!) "asc" else "desc"

            add(" order by log.$sortField $order ")
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
        }
    }

    private fun getWhereListExecutionLog(
        filter: ExecutionLogsFilter,
        queryParams: MutableList<Parameter>
    ): StringJoiner {
        return StringJoiner(QR_NL).apply {
            add(" where 1 = 1 ")

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

            filter.serviceExecutionStart?.let {
                add(" and log.serviceExecutionStart between :startServiceExecutionStart and :endServiceExecutionStart ")
                queryParams.add(Parameter(name = "startServiceExecutionStart", value = it.first))
                queryParams.add(Parameter(name = "endServiceExecutionStart", value = it.second))
            }

            filter.serviceExecutionEnd?.let {
                add(" and log.serviceExecutionEnd between :startServiceExecutionEnd and :endServiceExecutionEnd ")
                queryParams.add(Parameter(name = "startServiceExecutionEnd", value = it.first))
                queryParams.add(Parameter(name = "endServiceExecutionEnd", value = it.second))
            }

            filter.clientExecutionStart?.let {
                add(" and log.clientExecutionStart between :startClientExecutionStart and :endClientExecutionStart ")
                queryParams.add(Parameter(name = "startClientExecutionStart", value = it.first))
                queryParams.add(Parameter(name = "endClientExecutionStart", value = it.second))
            }

            filter.clientExecutionEnd?.let {
                add(" and log.clientExecutionEnd between :startClientExecutionEnd and :endClientExecutionEnd ")
                queryParams.add(Parameter(name = "startClientExecutionEnd", value = it.first))
                queryParams.add(Parameter(name = "endClientExecutionEnd", value = it.second))
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
}