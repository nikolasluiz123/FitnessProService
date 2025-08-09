package br.com.fitnesspro.authentication.repository.jpa

import br.com.fitnesspro.jpa.helper.Constants.QR_NL
import br.com.fitnesspro.jpa.query.Parameter
import br.com.fitnesspro.jpa.query.setParameters
import br.com.fitnesspro.models.serviceauth.Application
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class CustomApplicationRepositoryImpl: ICustomApplicationRepository {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    override fun getListApplication(): List<Application> {
        val queryParams = mutableListOf<Parameter>()

        val select = StringJoiner(QR_NL).apply {
            add(" select a ")
        }

        val from = StringJoiner(QR_NL).apply {
            add(" from ${Application::class.java.name} a ")
        }

        val where = StringJoiner(QR_NL).apply {
            add(" where a.active = true ")
        }

        val sql = StringJoiner(QR_NL).apply {
            add(select.toString())
            add(from.toString())
            add(where.toString())
        }

        val query = entityManager.createQuery(sql.toString(), Application::class.java)
        query.setParameters(queryParams)

        return query.resultList
    }
}