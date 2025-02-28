package br.com.fitnesspro.service.repository.general.academy

import br.com.fitnesspro.service.models.general.PersonAcademyTime
import br.com.fitnesspro.service.repository.common.helper.Constants.QR_NL
import br.com.fitnesspro.service.repository.common.query.Parameter
import br.com.fitnesspro.service.repository.common.query.getResultList
import br.com.fitnesspro.service.repository.common.query.setParameters
import br.com.fitnesspro.shared.communication.dtos.general.AcademyDTO
import br.com.fitnesspro.shared.communication.filter.CommonImportFilter
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
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

    override fun getAcademiesImport(filter: CommonImportFilter, pageInfos: ImportPageInfos): List<AcademyDTO> {
        val params = mutableListOf<Parameter>()

        val select = StringJoiner(QR_NL).apply {
            add(" select a.id as id, ")
            add("        a.creation_date as creationDate, ")
            add("        a.update_date as updateDate, ")
            add("        a.creation_user_id as creationUserId, ")
            add("        a.update_user_id as updateUserId, ")
            add("        a.name as name, ")
            add("        a.address as address, ")
            add("        a.phone as phone, ")
            add("        a.active as active ")
        }

        val from = StringJoiner(QR_NL).apply {
            add(" from academy a ")
        }

        val where = StringJoiner(QR_NL).apply {
            add(" where 1 = 1 ")

            filter.lastUpdateDate?.let {
                add(" and a.update_date >= :pLastUpdateDate ")
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

        val result = query.getResultList(AcademyDTO::class.java)

        return result
    }
}