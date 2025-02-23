package br.com.fitnesspro.service.repository.scheduler

import br.com.fitnesspro.models.general.enums.EnumUserType
import br.com.fitnesspro.models.scheduler.enums.EnumSchedulerSituation
import br.com.fitnesspro.service.repository.common.helper.Constants.QR_NL
import br.com.fitnesspro.service.repository.common.query.Parameter
import br.com.fitnesspro.service.repository.common.query.getResultList
import br.com.fitnesspro.service.repository.common.query.setParameters
import br.com.fitnesspro.shared.communication.dtos.scheduler.SchedulerDTO
import br.com.fitnesspro.shared.communication.filter.CommonImportFilter
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import jakarta.persistence.EntityManager
import jakarta.persistence.NoResultException
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

@Repository
class CustomSchedulerRepositoryImpl: ICustomSchedulerRepository {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    override fun getHasSchedulerConflict(
        schedulerId: String?,
        personId: String,
        userType: EnumUserType,
        scheduledDate: LocalDate,
        start: LocalTime,
        end: LocalTime
    ): Boolean {
        val params = mutableListOf<Parameter>()

        val select = StringJoiner(QR_NL).apply {
            add(" select 1 ")
        }

        val from = StringJoiner(QR_NL).apply {
            add(" from scheduler ")
        }

        val where = StringJoiner(QR_NL).apply {
            params.add(Parameter("pTimeStart", start))
            params.add(Parameter("pTimeEnd", end))
            params.add(Parameter("pScheduledDate", scheduledDate))
            params.add(Parameter("pSituationCancelled", EnumSchedulerSituation.CANCELLED))
            params.add(Parameter("pPersonId", personId))

            add(" where active = true ")
            add(" and ( ")
            add("       time_start between :pTimeStart and :pTimeEnd ")
            add("       or time_end between :pTimeStart and :pTimeEnd ")
            add("     ) ")
            add(" and scheduled_date = :pScheduledDate ")
            add(" and situation != :pSituationCancelled ")

            schedulerId?.let {
                params.add(Parameter("pSchedulerId", schedulerId))
                add(" and id != :pSchedulerId ")
            }

            when (userType) {
                EnumUserType.PERSONAL_TRAINER,
                EnumUserType.NUTRITIONIST -> {
                    add(" and professional_person_id = :pPersonId ")
                }

                EnumUserType.ACADEMY_MEMBER -> {
                    add(" and academy_member_person_id = :pPersonId ")
                }
            }

        }

        val sql = StringJoiner(QR_NL).apply {
            add(" select exists ( ")
            add(select.toString())
            add(from.toString())
            add(where.toString())
            add(" ) ")
        }

        val query = entityManager.createNativeQuery(sql.toString())
        query.setParameters(params)

        return try {
            query.singleResult as Boolean
        } catch (e: NoResultException) {
            false
        }
    }

    override fun getSchedulesImport(filter: CommonImportFilter, pageInfos: ImportPageInfos): List<SchedulerDTO> {
        val params = mutableListOf<Parameter>()

        val select = StringJoiner(QR_NL).apply {
            add(" select s.id as id, ")
            add("        s.creation_date as creationDate, ")
            add("        s.update_date as updateDate, ")
            add("        s.creation_user_id as creationUserId, ")
            add("        s.update_user_id as updateUserId, ")
            add("        s.active as active, ")
            add("        s.academy_member_person_id as academyMemberPersonId, ")
            add("        s.professional_person_id as professionalPersonId, ")
            add("        s.scheduled_date as scheduledDate, ")
            add("        s.time_start as timeStart, ")
            add("        s.time_end as timeEnd, ")
            add("        s.canceled_date as canceledDate, ")
            add("        s.situation as situation, ")
            add("        s.compromise_type as compromiseType, ")
            add("        s.observation as observation ")
        }

        val from = StringJoiner(QR_NL).apply {
            add(" from scheduler s ")
        }

        val where = StringJoiner(QR_NL).apply {
            add(" where 1 = 1 ")

            if (filter.onlyActives) {
                add(" and s.active = true ")
            }

            filter.lastUpdateDate?.let {
                add(" and s.update_date >= :pLastUpdateDate ")
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

        val result = query.getResultList(SchedulerDTO::class.java)

        return result
    }
}