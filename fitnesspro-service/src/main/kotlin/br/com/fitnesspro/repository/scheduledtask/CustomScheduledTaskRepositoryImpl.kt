package br.com.fitnesspro.repository.scheduledtask

import br.com.fitnesspro.models.scheduledtask.ScheduledTask
import br.com.fitnesspro.repository.common.helper.Constants.QR_NL
import br.com.fitnesspro.repository.common.query.Parameter
import br.com.fitnesspro.repository.common.query.setParameters
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class CustomScheduledTaskRepositoryImpl: ICustomScheduledTaskRepository {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    override fun getListScheduledTask(): List<ScheduledTask> {
        val queryParams = mutableListOf<Parameter>()

        val select = StringJoiner(QR_NL).apply {
            add(" select task ")
        }

        val from = StringJoiner(QR_NL).apply {
            add(" from ${ScheduledTask::class.java.name} task ")
        }

        val where = StringJoiner(QR_NL).apply {
            add(" where task.active = true ")
        }

        val sql = StringJoiner(QR_NL).apply {
            add(select.toString())
            add(from.toString())
            add(where.toString())
        }

        val query = entityManager.createQuery(sql.toString(), ScheduledTask::class.java)
        query.setParameters(queryParams)

        return query.resultList
    }

}