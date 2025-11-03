package br.com.fitnesspro.common.repository.jpa.logs

import br.com.fitnesspro.common.scheduledtasks.config.DeleteOldExecutionLogsConfig
import br.com.fitnesspro.core.extensions.dateTimeNow
import br.com.fitnesspro.jpa.helper.Constants.QR_NL
import br.com.fitnesspro.jpa.query.Parameter
import br.com.fitnesspro.jpa.query.setParameters
import br.com.fitnesspro.models.logs.ExecutionLog
import br.com.fitnesspro.models.logs.ExecutionLogPackage
import br.com.fitnesspro.models.logs.ExecutionLogSubPackage
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class CustomDeleteOldExecutionsLogsRepositoryImpl: ICustomDeleteOldExecutionLogsRepository {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

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

    override fun getIdsExecutionLogSubPackageDelete(executionLogPackageId: String): List<String> {
        val queryParams = mutableListOf<Parameter>()

        val select = StringJoiner(QR_NL).apply {
            add(" select subPackage.id as id ")
        }

        val from = StringJoiner(QR_NL).apply {
            add(" from ${ExecutionLogSubPackage::class.java.name} subPackage ")
        }

        val where = StringJoiner(QR_NL).apply {
            add(" where subPackage.executionLogPackage.id = :pId ")
            queryParams.add(Parameter(name = "pId", value = executionLogPackageId))
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