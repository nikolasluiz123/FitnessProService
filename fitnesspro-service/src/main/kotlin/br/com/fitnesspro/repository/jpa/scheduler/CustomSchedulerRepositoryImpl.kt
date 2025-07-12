package br.com.fitnesspro.repository.jpa.scheduler

import br.com.fitnesspro.models.scheduler.Scheduler
import br.com.fitnesspro.repository.common.extensions.getOffsetDateTime
import br.com.fitnesspro.repository.common.extensions.getString
import br.com.fitnesspro.repository.common.helper.Constants.QR_NL
import br.com.fitnesspro.repository.common.query.Parameter
import br.com.fitnesspro.repository.common.query.getResultList
import br.com.fitnesspro.repository.common.query.setParameters
import br.com.fitnesspro.shared.communication.enums.general.EnumUserType
import br.com.fitnesspro.shared.communication.enums.scheduler.EnumSchedulerSituation
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.CommonImportFilter
import br.com.fitnesspro.to.TOSchedulerAntecedenceNotificationInfo
import jakarta.persistence.EntityManager
import jakarta.persistence.NoResultException
import jakarta.persistence.PersistenceContext
import jakarta.persistence.Tuple
import org.springframework.stereotype.Repository
import java.time.OffsetDateTime
import java.util.*

@Repository
class CustomSchedulerRepositoryImpl: ICustomSchedulerRepository {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    override fun getHasSchedulerConflict(
        schedulerId: String?,
        personId: String,
        userType: EnumUserType,
        start: OffsetDateTime,
        end: OffsetDateTime
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
            params.add(Parameter("pSituationCancelled", EnumSchedulerSituation.CANCELLED))
            params.add(Parameter("pPersonId", personId))

            add(" where active = true ")
            add(" and ( ")
            add("       date_time_start between :pTimeStart and :pTimeEnd ")
            add("       or date_time_end between :pTimeStart and :pTimeEnd ")
            add("     ) ")
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

                EnumUserType.ADMINISTRATOR -> {
                    throw IllegalArgumentException("Valor inválido para userType")
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

    override fun getSchedulesImport(filter: CommonImportFilter, pageInfos: ImportPageInfos): List<Scheduler> {
        val params = mutableListOf<Parameter>()

        val select = StringJoiner(QR_NL).apply {
            add(" select scheduler ")
        }

        val from = StringJoiner(QR_NL).apply {
            add(" from ${Scheduler::class.java.name} scheduler ")
        }

        val where = StringJoiner(QR_NL).apply {
            add(" where 1 = 1 ")

            filter.lastUpdateDate?.let {
                add(" and scheduler.updateDate >= :pLastUpdateDate ")
                params.add(Parameter(name = "pLastUpdateDate", value = it))
            }
        }

        val sql = StringJoiner(QR_NL).apply {
            add(select.toString())
            add(from.toString())
            add(where.toString())
        }

        val query = entityManager.createQuery(sql.toString(), Scheduler::class.java)
        query.setParameters(params)
        query.firstResult = pageInfos.pageSize * pageInfos.pageNumber
        query.maxResults = pageInfos.pageSize

        return query.getResultList(Scheduler::class.java)
    }

    override fun getListTOSchedulerAntecedenceNotificationInfo(): List<TOSchedulerAntecedenceNotificationInfo> {
        val params = mutableListOf<Parameter>()

        val select = StringJoiner(QR_NL).apply {
            add(" select config.person_id as personToSendNotificationId, ")
            add("        scheduler.id as schedulerId, ")
            add("        (case when member.id = config.person_id then professional.name else member.name end) as otherPersonName, ")
            add("        scheduler.date_time_start as dateTimeStart ")
        }

        val from = StringJoiner(QR_NL).apply {
            add(" from scheduler_config config ")
            add(" inner join scheduler on (scheduler.academy_member_person_id = config.person_id or scheduler.professional_person_id = config.person_id) ")
            add(" inner join person member on member.id = scheduler.academy_member_person_id ")
            add(" inner join person professional on professional.id = scheduler.professional_person_id ")
        }

        val where = StringJoiner(QR_NL).apply {
            add(" where config.active = true ")
            add(" and scheduler.active = true ")
            add(" and scheduler.notified_antecedence = false ")
            add(" and scheduler.situation in (:pScheduled, :pConfirmed) ")
            add(" and date_trunc('minute', current_timestamp + make_interval(mins => config.notification_antecedence_time)) = date_trunc('minute', scheduler.date_time_start) ")

            params.add(Parameter("pScheduled", EnumSchedulerSituation.SCHEDULED.ordinal))
            params.add(Parameter("pConfirmed", EnumSchedulerSituation.CONFIRMED.ordinal))
        }

        val sql = StringJoiner(QR_NL).apply {
            add(select.toString())
            add(from.toString())
            add(where.toString())
        }

        val query = entityManager.createNativeQuery(sql.toString(), Tuple::class.java)
        query.setParameters(params)

        return query.getResultList(Tuple::class.java).map {
            TOSchedulerAntecedenceNotificationInfo(
                personToSendNotificationId = it.getString("personToSendNotificationId")!!,
                schedulerId = it.getString("schedulerId")!!,
                otherPersonName = it.getString("otherPersonName")!!,
                dateTimeStart = it.getOffsetDateTime("dateTimeStart")!!
            )
        }
    }
}