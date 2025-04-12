package br.com.fitnesspro.service.repository.serviceauth

import br.com.fitnesspro.service.models.serviceauth.Application
import br.com.fitnesspro.service.repository.common.helper.Constants.QR_NL
import br.com.fitnesspro.service.repository.common.query.Parameter
import br.com.fitnesspro.service.repository.common.query.setParameters
import br.com.fitnesspro.shared.communication.query.filter.ApplicationFilter
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class CustomApplicationRepositoryImpl: ICustomApplicationRepository {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    override fun getListApplication(filter: ApplicationFilter): List<Application> {
        val queryParams = mutableListOf<Parameter>()

        val select = StringJoiner(QR_NL).apply {
            add(" select a ")
        }

        val from = StringJoiner(QR_NL).apply {
            add(" from ${Application::class.java.name} a ")
        }

        val where = StringJoiner(QR_NL).apply {
            add(" where a.ativo = true ")

            filter.name?.let {
                add(" and a.name like :pName ")
                queryParams.add(Parameter("pName", "%$it%"))
            }
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