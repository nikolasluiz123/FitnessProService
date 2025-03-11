package br.com.fitnesspro.service.repository.general.person

import br.com.fitnesspro.service.models.general.Person
import br.com.fitnesspro.service.models.general.User
import br.com.fitnesspro.service.repository.common.helper.Constants.QR_NL
import br.com.fitnesspro.service.repository.common.query.Parameter
import br.com.fitnesspro.service.repository.common.query.getResultList
import br.com.fitnesspro.service.repository.common.query.setParameters
import br.com.fitnesspro.shared.communication.query.filter.CommonImportFilter
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class CustomPersonRepositoryImpl: ICustomPersonRepository {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    override fun getPersonsImport(filter: CommonImportFilter, pageInfos: ImportPageInfos): List<Person> {
        val params = mutableListOf<Parameter>()

        val select = StringJoiner(QR_NL).apply {
            add(" select p ")
        }

        val from = StringJoiner(QR_NL).apply {
            add(" from ${Person::class.java.name} p ")
        }

        val where = StringJoiner(QR_NL).apply {
            add(" where 1 = 1 ")

            filter.lastUpdateDate?.let {
                add(" and p.updateDate >= :pLastUpdateDate ")
                params.add(Parameter(name = "pLastUpdateDate", value = it))
            }
        }

        val sql = StringJoiner(QR_NL).apply {
            add(select.toString())
            add(from.toString())
            add(where.toString())
        }

        val query = entityManager.createQuery(sql.toString(), Person::class.java)
        query.setParameters(params)
        query.firstResult = pageInfos.pageSize * pageInfos.pageNumber
        query.maxResults = pageInfos.pageSize

        val result = query.getResultList(Person::class.java)

        return result
    }

    override fun getUsersImport(filter: CommonImportFilter, pageInfos: ImportPageInfos): List<User> {
        val params = mutableListOf<Parameter>()

        val select = StringJoiner(QR_NL).apply {
            add(" select u ")
        }

        val from = StringJoiner(QR_NL).apply {
            add(" from ${User::class.java.name} u ")
        }

        val where = StringJoiner(QR_NL).apply {
            add(" where 1 = 1 ")

            filter.lastUpdateDate?.let {
                add(" and u.updateDate >= :pLastUpdateDate ")
                params.add(Parameter(name = "pLastUpdateDate", value = it))
            }
        }

        val sql = StringJoiner(QR_NL).apply {
            add(select.toString())
            add(from.toString())
            add(where.toString())
        }

        val query = entityManager.createQuery(sql.toString(), User::class.java)
        query.setParameters(params)
        query.firstResult = pageInfos.pageSize * pageInfos.pageNumber
        query.maxResults = pageInfos.pageSize

        val result = query.getResultList(User::class.java)

        return result
    }
}