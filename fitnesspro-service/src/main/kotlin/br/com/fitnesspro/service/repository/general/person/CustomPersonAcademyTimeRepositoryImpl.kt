package br.com.fitnesspro.service.repository.general.person

import br.com.fitnesspro.shared.communication.dtos.general.PersonAcademyTimeDTO
import br.com.fitnesspro.service.models.general.PersonAcademyTime
import br.com.fitnesspro.service.repository.common.filter.CommonImportFilter
import br.com.fitnesspro.service.repository.common.helper.Constants.QR_NL
import br.com.fitnesspro.service.repository.common.paging.ImportPageInfos
import br.com.fitnesspro.service.repository.common.query.Parameter
import br.com.fitnesspro.service.repository.common.query.getResultList
import br.com.fitnesspro.service.repository.common.query.setParameters
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
    ): List<PersonAcademyTimeDTO> {
        val params = mutableListOf<Parameter>()

        val select = StringJoiner(QR_NL).apply {
            add(" select pat.id as id, ")
            add("        pat.person_id as personId, ")
            add("        pat.academy_id as academyId, ")
            add("        pat.time_start as timeStart, ")
            add("        pat.time_end as timeEnd, ")
            add("        pat.day_week as dayOfWeek, ")
            add("        pat.active as active, ")
            add("        pat.creation_date as creationDate, ")
            add("        pat.update_date as updateDate ")
        }

        val from = StringJoiner(QR_NL).apply {
            add(" from person_academy_time pat ")
        }

        val where = StringJoiner(QR_NL).apply {
            add(" where 1 = 1 ")

            if (filter.onlyActives) {
                add(" and pat.active = true ")
            }

            filter.lastUpdateDate?.let {
                add(" and pat.update_date >= :pLastUpdateDate ")
                params.add(Parameter(name = "pLastUpdateDate", value = it))
            }
        }

        val sql = StringJoiner(QR_NL).apply {
            add(select.toString())
            add(from.toString())
            add(where.toString())
        }

        val query = entityManager.createNativeQuery(sql.toString())
        query.setParameters(params)
        query.firstResult = pageInfos.pageSize * pageInfos.pageNumber
        query.maxResults = pageInfos.pageSize

        val result = query.getResultList(PersonAcademyTimeDTO::class.java)

        return result
    }

}