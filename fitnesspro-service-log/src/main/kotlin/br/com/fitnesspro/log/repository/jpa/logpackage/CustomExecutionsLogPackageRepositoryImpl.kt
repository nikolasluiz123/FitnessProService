package br.com.fitnesspro.log.repository.jpa.logpackage

import br.com.fitnesspro.jpa.helper.Constants.QR_NL
import br.com.fitnesspro.jpa.query.Parameter
import br.com.fitnesspro.jpa.query.setParameters
import br.com.fitnesspro.models.logs.ExecutionLogPackage
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import jakarta.persistence.Tuple
import org.springframework.stereotype.Repository
import java.time.Duration
import java.time.LocalDateTime
import java.util.*

@Repository
class CustomExecutionsLogPackageRepositoryImpl: ICustomExecutionsLogPackageRepository {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    override fun getExecutionProcessingTime(logId: String): ExecutionProcessingTime {
        val queryParams = mutableListOf<Parameter>()

        val select = StringJoiner(QR_NL).apply {
            add(" select logPackage.serviceExecutionStart as serviceStart, ")
            add("        logPackage.serviceExecutionEnd as serviceEnd, ")
            add("        logPackage.clientExecutionStart as clientStart, ")
            add("        logPackage.clientExecutionEnd as clientEnd ")
        }

        val from = StringJoiner(QR_NL).apply {
            add(" from ${ExecutionLogPackage::class.java.name} logPackage ")
        }

        val where = StringJoiner(QR_NL).apply {
            add(" where logPackage.executionLog.id = :pLogId ")
            queryParams.add(Parameter(name = "pLogId", value = logId))
        }

        val sql = StringJoiner(QR_NL).apply {
            add(select.toString())
            add(from.toString())
            add(where.toString())
        }

        val query = entityManager.createQuery(sql.toString(), Tuple::class.java)
        query.setParameters(queryParams)

        var totalServiceDuration = 0L
        var totalClientDuration = 0L

        query.resultList.forEach { tuple ->
            val serviceStart = tuple.get("serviceStart", LocalDateTime::class.java)
            val serviceEnd = tuple.get("serviceEnd", LocalDateTime::class.java)
            val clientStart = tuple.get("clientStart", LocalDateTime::class.java)
            val clientEnd = tuple.get("clientEnd", LocalDateTime::class.java)

            if (serviceStart != null && serviceEnd != null) {
                totalServiceDuration += Duration.between(serviceStart, serviceEnd).toMillis()
            }

            if (clientStart != null && clientEnd != null) {
                totalClientDuration += Duration.between(clientStart, clientEnd).toMillis()
            }
        }

        return ExecutionProcessingTime(
            service = if (totalServiceDuration > 0) totalServiceDuration else null,
            client = if (totalClientDuration > 0) totalClientDuration else null
        )
    }

}