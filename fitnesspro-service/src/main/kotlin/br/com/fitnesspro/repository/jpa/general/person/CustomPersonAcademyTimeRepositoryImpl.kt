package br.com.fitnesspro.repository.jpa.general.person

import br.com.fitnesspro.models.general.PersonAcademyTime
import br.com.fitnesspro.repository.common.helper.Constants.QR_NL
import br.com.fitnesspro.repository.common.query.Parameter
import br.com.fitnesspro.repository.common.query.getResultList
import br.com.fitnesspro.repository.common.query.setParameters
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.CommonImportFilter
import jakarta.persistence.EntityManager
import jakarta.persistence.NoResultException
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Repository
import java.time.DayOfWeek
import java.time.LocalTime
import java.util.*

@Repository
class CustomPersonAcademyTimeRepositoryImpl: ICustomPersonAcademyTimeRepository {
    
    @PersistenceContext
    private lateinit var entityManager: EntityManager
    
    override fun getConflictPersonAcademyTime(
        personAcademyTimeId: String?,
        personId: String,
        dayOfWeek: DayOfWeek,
        start: LocalTime,
        end: LocalTime
    ): PersonAcademyTime? {
        val params = mutableListOf<Parameter>()

        val select = StringJoiner(QR_NL).apply {
            add(" select pat ")
        }

        val from = StringJoiner(QR_NL).apply {
            add(" from ${PersonAcademyTime::class.java.name} pat ")
        }

        val where = StringJoiner(QR_NL).apply {
            params.add(Parameter(name = "pPersonId", value = personId))
            params.add(Parameter(name = "pDayOfWeek", value = dayOfWeek))
            params.add(Parameter(name = "pStart", value = start))
            params.add(Parameter(name = "pEnd", value = end))

            add(" where pat.person.id = :pPersonId ")
            add(" and pat.dayOfWeek = :pDayOfWeek ")
            add(" and ( ")
            add("       pat.timeStart between :pStart and :pEnd ")
            add("       or pat.timeEnd between :pStart and :pEnd ")
            add("     ) ")

            personAcademyTimeId?.let {
                add(" and pat.id != :pPersonAcademyTimeId ")
                params.add(Parameter(name = "pPersonAcademyTimeId", value = personAcademyTimeId))
            }
        }

        val sql = StringJoiner(QR_NL).apply {
            add(select.toString())
            add(from.toString())
            add(where.toString())
        }

        val query = entityManager.createQuery(sql.toString(), PersonAcademyTime::class.java)
        query.setParameters(params)

        return try {
            query.singleResult
        } catch (ex: NoResultException) {
            return null
        }
    }

    override fun getPersonAcademyTimesImport(
        filter: CommonImportFilter,
        pageInfos: ImportPageInfos
    ): List<PersonAcademyTime> {
        val params = mutableListOf<Parameter>()

        val select = StringJoiner(QR_NL).apply {
            add(" select pat ")
        }

        val from = StringJoiner(QR_NL).apply {
            add(" from ${PersonAcademyTime::class.java.name} pat ")
        }

        val where = StringJoiner(QR_NL).apply {
            add(" where 1 = 1 ")

            filter.lastUpdateDate?.let {
                add(" and pat.updateDate >= :pLastUpdateDate ")
                params.add(Parameter(name = "pLastUpdateDate", value = it))
            }
        }

        val sql = StringJoiner(QR_NL).apply {
            add(select.toString())
            add(from.toString())
            add(where.toString())
        }

        val query = entityManager.createQuery(sql.toString(), PersonAcademyTime::class.java)
        query.setParameters(params)

        query.firstResult = pageInfos.pageSize * pageInfos.pageNumber
        query.maxResults = pageInfos.pageSize

        return query.getResultList(PersonAcademyTime::class.java)
    }

}