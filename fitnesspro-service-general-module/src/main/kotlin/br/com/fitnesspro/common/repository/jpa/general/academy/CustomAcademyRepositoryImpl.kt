package br.com.fitnesspro.common.repository.jpa.general.academy

import br.com.fitnesspro.jpa.helper.Constants.QR_NL
import br.com.fitnesspro.jpa.query.Parameter
import br.com.fitnesspro.jpa.query.getResultList
import br.com.fitnesspro.jpa.query.setParameters
import br.com.fitnesspro.models.general.Academy
import br.com.fitnesspro.models.general.PersonAcademyTime
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.paging.PageInfos
import br.com.fitnesspro.shared.communication.query.filter.AcademyFilter
import br.com.fitnesspro.shared.communication.query.filter.importation.CommonImportFilter
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Repository
import java.time.DayOfWeek
import java.util.*

@Repository
class CustomAcademyRepositoryImpl: ICustomAcademyRepository {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    override fun getPersonAcademyTimeList(personId: String, academyId: String?, dayOfWeek: DayOfWeek?): List<PersonAcademyTime> {
        val params = mutableListOf<Parameter>()
        params.add(Parameter("pPersonId", personId))

        val select = StringJoiner(QR_NL).apply {
            add(" select pat ")
        }

        val from = StringJoiner(QR_NL).apply {
            add(" from ${PersonAcademyTime::class.java.name} pat ")
        }

        val where = StringJoiner(QR_NL).apply {
            add(" where pat.active = true ")
            add(" and pat.person.id = :pPersonId ")

            academyId?.let {
                params.add(Parameter("pAcademyId", academyId))
                add(" and pat.academy.id = :pAcademyId ")
            }

            dayOfWeek?.let {
                params.add(Parameter("pDayOfWeek", dayOfWeek))
                add(" and pat.dayOfWeek = :pDayOfWeek ")
            }
        }

        val sql = StringJoiner(QR_NL).apply {
            add(select.toString())
            add(from.toString())
            add(where.toString())
        }

        val query = entityManager.createQuery(sql.toString(), PersonAcademyTime::class.java)
        query.setParameters(params)

        return query.resultList
    }

    override fun getAcademiesImport(filter: CommonImportFilter, pageInfos: ImportPageInfos): List<Academy> {
        val params = mutableListOf<Parameter>()

        val select = StringJoiner(QR_NL).apply {
            add(" select a ")
        }

        val from = StringJoiner(QR_NL).apply {
            add(" from ${Academy::class.java.name} a ")
        }

        val where = StringJoiner(QR_NL).apply {
            add(" where 1 = 1 ")

            filter.lastUpdateDateMap[Academy::class.simpleName!!]?.let {
                add(" and (a.updateDate > :pLastUpdateDate OR (a.updateDate = :pLastUpdateDate AND a.id > :pCursorId)) ")
                params.add(Parameter(name = "pLastUpdateDate", value = it))
                params.add(Parameter(name = "pCursorId", value = pageInfos.cursorIdMap[Academy::class.simpleName!!] ?: ""))
            }
        }

        val orderBy = StringJoiner(QR_NL).apply {
            add(" order by a.updateDate asc, a.id asc ")
        }

        val sql = StringJoiner(QR_NL).apply {
            add(select.toString())
            add(from.toString())
            add(where.toString())
            add(orderBy.toString())
        }

        val query = entityManager.createQuery(sql.toString(), Academy::class.java)
        query.setParameters(params)
        query.maxResults = pageInfos.pageSize

        val result = query.getResultList(Academy::class.java)

        return result
    }

    override fun getListAcademy(filter: AcademyFilter, pageInfos: PageInfos): List<Academy> {
        val queryParams = mutableListOf<Parameter>()

        val select = StringJoiner(QR_NL).apply {
            add(" select a ")
        }

        val from = getFromListAcademy()

        val where = getWhereListAcademy(filter, queryParams)

        val orderBy = StringJoiner(QR_NL).apply {
            if (filter.sort == null) {
                add(" order by a.name ")
            } else {
                val sortField = filter.sort?.field?.fieldName!!
                val order = if (filter.sort?.asc!!) "asc" else "desc"

                add(" order by a.$sortField $order ")
            }
        }

        val sql = StringJoiner(QR_NL).apply {
            add(select.toString())
            add(from.toString())
            add(where.toString())
            add(orderBy.toString())
        }

        val query = entityManager.createQuery(sql.toString(), Academy::class.java)
        query.setParameters(queryParams)

        query.firstResult = pageInfos.pageSize * pageInfos.pageNumber
        query.maxResults = pageInfos.pageSize

        return query.resultList
    }

    private fun getFromListAcademy(): StringJoiner {
        return StringJoiner(QR_NL).apply {
            add(" from ${Academy::class.java.name} a ")
        }
    }

    private fun getWhereListAcademy(filter: AcademyFilter, queryParams: MutableList<Parameter>): StringJoiner {
        return StringJoiner(QR_NL).apply {
            add(" where a.active = true ")

            filter.name?.let {
                add(" and lower(a.name) like lower(:name) ")
                queryParams.add(Parameter(name = "name", value = "%$it%"))
            }

            filter.address?.let {
                add(" and lower(a.address) like lower(:address ")
                queryParams.add(Parameter(name = "address", value = "%$it%"))
            }

            filter.phone?.let {
                add(" and a.phone like :phone ")
                queryParams.add(Parameter(name = "phone", value = "%$it%"))
            }
        }
    }

    override fun getCountListAcademy(filter: AcademyFilter): Int {
        val queryParams = mutableListOf<Parameter>()

        val select = StringJoiner(QR_NL).apply {
            add(" select count(a.id) ")
        }

        val from = getFromListAcademy()

        val where = getWhereListAcademy(filter, queryParams)

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