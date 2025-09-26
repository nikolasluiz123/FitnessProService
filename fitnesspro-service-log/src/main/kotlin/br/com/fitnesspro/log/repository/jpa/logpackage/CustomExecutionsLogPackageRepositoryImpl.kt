package br.com.fitnesspro.log.repository.jpa.logpackage

import br.com.fitnesspro.jpa.helper.Constants.QR_NL
import br.com.fitnesspro.jpa.query.Parameter
import br.com.fitnesspro.jpa.query.setParameters
import br.com.fitnesspro.models.logs.ExecutionLogPackage
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Repository
import java.time.Duration
import java.time.LocalDateTime
import java.util.*

@Repository
class CustomExecutionsLogPackageRepositoryImpl: ICustomExecutionsLogPackageRepository {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    override fun getExecutionProcessingTime(logId: String): ExecutionProcessingTime {
        val serviceStart = getServiceStartTime(logId)
        val serviceEnd = getServiceEndTime(logId)

        val clientStart = getClientStartTime(logId)
        val clientEnd = getClientEndTime(logId)

        val serviceDuration = if (serviceStart != null && serviceEnd != null) {
            Duration.between(serviceStart, serviceEnd).toMillis()
        } else {
            null
        }

        val clientDuration = if (clientStart != null && clientEnd != null) {
            Duration.between(clientStart, clientEnd).toMillis()
        } else {
            null
        }

        return ExecutionProcessingTime(service = serviceDuration, client = clientDuration)
    }

    private fun getServiceStartTime(logId: String): LocalDateTime? {
        val queryParams = mutableListOf<Parameter>()

        val select = StringJoiner(QR_NL).apply {
            add(" select logPackage.serviceExecutionStart as serviceStart ")
        }

        val from = StringJoiner(QR_NL).apply {
            add(" from ${ExecutionLogPackage::class.java.name} logPackage ")
        }

        val where = StringJoiner(QR_NL).apply {
            add(" where logPackage.executionLog.id = :pLogId ")
            queryParams.add(Parameter(name = "pLogId", value = logId))
        }

        val orderBy = StringJoiner(QR_NL).apply {
            add(" order by logPackage.serviceExecutionStart ")
        }

        val sql = StringJoiner(QR_NL).apply {
            add(select.toString())
            add(from.toString())
            add(where.toString())
            add(orderBy.toString())
        }

        val query = entityManager.createQuery(sql.toString(), LocalDateTime::class.java)
        query.setParameters(queryParams)

        return query.resultList.firstOrNull()
    }

    private fun getClientStartTime(logId: String): LocalDateTime? {
        val queryParams = mutableListOf<Parameter>()

        val select = StringJoiner(QR_NL).apply {
            add(" select logPackage.clientExecutionStart as clientStart ")
        }

        val from = StringJoiner(QR_NL).apply {
            add(" from ${ExecutionLogPackage::class.java.name} logPackage ")
        }

        val where = StringJoiner(QR_NL).apply {
            add(" where logPackage.executionLog.id = :pLogId ")
            queryParams.add(Parameter(name = "pLogId", value = logId))
        }

        val orderBy = StringJoiner(QR_NL).apply {
            add(" order by logPackage.clientExecutionStart ")
        }

        val sql = StringJoiner(QR_NL).apply {
            add(select.toString())
            add(from.toString())
            add(where.toString())
            add(orderBy.toString())
        }

        val query = entityManager.createQuery(sql.toString(), LocalDateTime::class.java)
        query.setParameters(queryParams)

        return query.resultList.firstOrNull()
    }

    private fun getServiceEndTime(logId: String): LocalDateTime? {
        val queryParams = mutableListOf<Parameter>()

        val select = StringJoiner(QR_NL).apply {
            add(" select logPackage.serviceExecutionEnd as serviceEnd ")
        }

        val from = StringJoiner(QR_NL).apply {
            add(" from ${ExecutionLogPackage::class.java.name} logPackage ")
        }

        val where = StringJoiner(QR_NL).apply {
            add(" where logPackage.executionLog.id = :pLogId ")
            queryParams.add(Parameter(name = "pLogId", value = logId))
        }

        val orderBy = StringJoiner(QR_NL).apply {
            add(" order by logPackage.serviceExecutionEnd desc ")
        }

        val sql = StringJoiner(QR_NL).apply {
            add(select.toString())
            add(from.toString())
            add(where.toString())
            add(orderBy.toString())
        }

        val query = entityManager.createQuery(sql.toString(), LocalDateTime::class.java)
        query.setParameters(queryParams)

        return query.resultList.firstOrNull()
    }

    private fun getClientEndTime(logId: String): LocalDateTime? {
        val queryParams = mutableListOf<Parameter>()

        val select = StringJoiner(QR_NL).apply {
            add(" select logPackage.clientExecutionEnd as clientEnd ")
        }

        val from = StringJoiner(QR_NL).apply {
            add(" from ${ExecutionLogPackage::class.java.name} logPackage ")
        }

        val where = StringJoiner(QR_NL).apply {
            add(" where logPackage.executionLog.id = :pLogId ")
            queryParams.add(Parameter(name = "pLogId", value = logId))
        }

        val orderBy = StringJoiner(QR_NL).apply {
            add(" order by logPackage.clientExecutionEnd desc ")
        }

        val sql = StringJoiner(QR_NL).apply {
            add(select.toString())
            add(from.toString())
            add(where.toString())
            add(orderBy.toString())
        }

        val query = entityManager.createQuery(sql.toString(), LocalDateTime::class.java)
        query.setParameters(queryParams)

        return query.resultList.firstOrNull()
    }

}