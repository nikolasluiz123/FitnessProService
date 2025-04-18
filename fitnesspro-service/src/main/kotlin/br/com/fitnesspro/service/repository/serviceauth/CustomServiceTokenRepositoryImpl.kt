package br.com.fitnesspro.service.repository.serviceauth

import br.com.fitnesspro.service.models.serviceauth.ServiceToken
import br.com.fitnesspro.service.repository.common.helper.Constants.QR_NL
import br.com.fitnesspro.service.repository.common.query.Parameter
import br.com.fitnesspro.service.repository.common.query.getResultList
import br.com.fitnesspro.service.repository.common.query.setParameters
import br.com.fitnesspro.shared.communication.paging.PageInfos
import br.com.fitnesspro.shared.communication.query.filter.ServiceTokenFilter
import jakarta.persistence.EntityManager
import jakarta.persistence.NoResultException
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class CustomServiceTokenRepositoryImpl: ICustomServiceTokenRepository {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    override fun getListServiceToken(filter: ServiceTokenFilter, pageInfos: PageInfos): List<ServiceToken> {
        val params = mutableListOf<Parameter>()

        val select = StringJoiner(QR_NL).apply {
            add(" select t ")
        }

        val from = getFromListServiceToken()

        val where = getWhereListServiceToken(filter, params)

        val orderBy = StringJoiner(QR_NL).apply {
            if (filter.sort.isEmpty()) {
                add(" order by t.creationDate desc, t.type asc ")
            } else {
                add(" order by ")

                filter.sort.forEachIndexed { index, sort ->
                    val sortField = sort.field.fieldName
                    val order = if (sort.asc) "asc" else "desc"
                    val comma = if (index == filter.sort.lastIndex) "" else ", "

                    add(" t.$sortField $order$comma ")
                }
            }
        }

        val sql = StringJoiner(QR_NL).apply {
            add(select.toString())
            add(from.toString())
            add(where.toString())
            add(orderBy.toString())
        }

        val query = entityManager.createQuery(sql.toString(), ServiceToken::class.java)
        query.setParameters(params)

        query.firstResult = pageInfos.pageSize * pageInfos.pageNumber
        query.maxResults = pageInfos.pageSize

        val result = query.getResultList(ServiceToken::class.java)

        return result
    }

    private fun getFromListServiceToken(): StringJoiner {
        return StringJoiner(QR_NL).apply {
            add(" from ${ServiceToken::class.java.name} t ")
            add(" left join t.user user ")
            add(" left join t.application application ")
            add(" left join t.device device ")
        }
    }

    private fun getWhereListServiceToken(
        filter: ServiceTokenFilter,
        params: MutableList<Parameter>
    ): StringJoiner {
        return StringJoiner(QR_NL).apply {
            add(" where 1 = 1 ")

            filter.creationDate?.let {
                add(" and t.creationDate between :pCreationDateStart and :pCreationDateEnd ")
                params.add(Parameter(name = "pCreationDateStart", value = it.first))
                params.add(Parameter(name = "pCreationDateEnd", value = it.second))
            }

            filter.expirationDate?.let {
                add(" and t.expirationDate between :pExpirationDateStart and :pExpirationDateEnd ")
                params.add(Parameter(name = "pExpirationDateStart", value = it.first))
                params.add(Parameter(name = "pExpirationDateEnd", value = it.second))
            }

            filter.tokenType?.let {
                add(" and t.type = :pTokenType ")
                params.add(Parameter(name = "pTokenType", value = it))
            }

            filter.userEmail?.let {
                add(" and user.email = :pUserEmail ")
                params.add(Parameter(name = "pUserEmail", value = it))
            }

            filter.deviceId?.let {
                add(" and device.id = :pDeviceId ")
                params.add(Parameter(name = "pDeviceId", value = it))
            }

            filter.applicationName?.let {
                add(" and application.name :pApplicationName ")
                params.add(Parameter(name = "pApplicationName", value = "%$it%"))
            }
        }
    }

    override fun getCountListServiceToken(filter: ServiceTokenFilter): Int {
        val queryParams = mutableListOf<Parameter>()

        val select = StringJoiner(QR_NL).apply {
            add(" select count(t.id) ")
        }

        val from = getFromListServiceToken()

        val where = getWhereListServiceToken(filter, queryParams)

        val sql = StringJoiner(QR_NL).apply {
            add(select.toString())
            add(from.toString())
            add(where.toString())
        }

        val query = entityManager.createQuery(sql.toString(), Long::class.java)
        query.setParameters(queryParams)

        return query.singleResult.toInt()
    }

    override fun findValidServiceToken(jwtToken: String): ServiceToken? {
        val queryParams = mutableListOf<Parameter>()

        val select = StringJoiner(QR_NL).apply {
            add(" select t ")
        }

        val from = StringJoiner(QR_NL).apply {
            add(" from ${ServiceToken::class.java.name} t ")
        }

        val where = StringJoiner(QR_NL).apply {
            add(" where t.jwtToken = :pJwtToken ")
            add(" and (t.expirationDate > current_timestamp or t.expirationDate is null) ")

            queryParams.add(Parameter(name = "pJwtToken", value = jwtToken))
        }

        val sql = StringJoiner(QR_NL).apply {
            add(select.toString())
            add(from.toString())
            add(where.toString())
        }

        val query = entityManager.createQuery(sql.toString(), ServiceToken::class.java)
        query.setParameters(queryParams)

        return try {
            query.resultList.firstOrNull()
        } catch (ex: NoResultException) {
            null
        }
    }
}