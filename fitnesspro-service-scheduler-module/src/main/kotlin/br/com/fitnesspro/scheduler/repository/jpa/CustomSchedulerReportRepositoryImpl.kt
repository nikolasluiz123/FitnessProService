package br.com.fitnesspro.scheduler.repository.jpa

import br.com.fitnesspro.jpa.helper.Constants.QR_NL
import br.com.fitnesspro.jpa.query.Parameter
import br.com.fitnesspro.jpa.query.getResultList
import br.com.fitnesspro.jpa.query.setParameters
import br.com.fitnesspro.models.general.SchedulerReport
import br.com.fitnesspro.shared.communication.enums.report.EnumReportContext
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.SchedulerReportImportFilter
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class CustomSchedulerReportRepositoryImpl: ICustomSchedulerReportRepository {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    override fun getSchedulerReportsImport(
        filter: SchedulerReportImportFilter,
        pageInfos: ImportPageInfos
    ): List<SchedulerReport> {
        val params = mutableListOf<Parameter>()

        val select = StringJoiner(QR_NL).apply {
            add(" select sr ")
        }

        val from = StringJoiner(QR_NL).apply {
            add(" from ${SchedulerReport::class.java.name} sr ")
            add(" inner join sr.report report ")
        }

        val where = StringJoiner(QR_NL).apply {
            add(" where sr.person.id = :pPersonId ")

            params.add(Parameter(name = "pPersonId", value = filter.personId))

            filter.lastUpdateDateMap[SchedulerReport::class.simpleName!!]?.let {
                add(" and sr.updateDate > :pLastUpdateDate ")
                params.add(Parameter(name = "pLastUpdateDate", value = it))
            }
        }

        val orderBy = StringJoiner(QR_NL).apply {
            add(" order by sr.updateDate asc, sr.id asc ")
        }

        val sql = StringJoiner(QR_NL).apply {
            add(select.toString())
            add(from.toString())
            add(where.toString())
            add(orderBy.toString())
        }

        val query = entityManager.createQuery(sql.toString(), SchedulerReport::class.java)
        query.setParameters(params)
        query.maxResults = pageInfos.pageSize

        val result = query.getResultList(SchedulerReport::class.java)

        return result
    }

    override fun getReportIdsFromSchedulerDelete(context: EnumReportContext, personId: String): List<String> {
        val params = mutableListOf<Parameter>()

        val select = StringJoiner(QR_NL).apply {
            add(" select report.id ")
        }

        val from = StringJoiner(QR_NL).apply {
            add(" from ${SchedulerReport::class.java.name} sr ")
            add(" inner join sr.report report ")
        }

        val where = StringJoiner(QR_NL).apply {
            add(" where sr.person.id = :pPersonId ")
            add(" and sr.reportContext = :pContext ")

            params.add(Parameter(name = "pPersonId", value = personId))
            params.add(Parameter(name = "pContext", value = context))
        }

        val sql = StringJoiner(QR_NL).apply {
            add(select.toString())
            add(from.toString())
            add(where.toString())
        }

        val query = entityManager.createQuery(sql.toString(), String::class.java)
        query.setParameters(params)

        return query.getResultList(String::class.java)
    }

    override fun getSchedulerReportIdsDelete(context: EnumReportContext, authenticatedUserId: String, reportId: String?): List<String> {
        val params = mutableListOf<Parameter>()

        val select = StringJoiner(QR_NL).apply {
            add(" select sr.id ")
        }

        val from = StringJoiner(QR_NL).apply {
            add(" from ${SchedulerReport::class.java.name} sr ")
            add(" inner join sr.report report ")
        }

        val where = StringJoiner(QR_NL).apply {
            add(" where sr.person.user.id = :pPersonId ")
            add(" and sr.reportContext = :pContext ")

            params.add(Parameter(name = "pPersonId", value = authenticatedUserId))
            params.add(Parameter(name = "pContext", value = context))

            reportId?.let {
                add(" and report.id = :pReportId ")
                params.add(Parameter(name = "pReportId", value = it))
            }
        }

        val sql = StringJoiner(QR_NL).apply {
            add(select.toString())
            add(from.toString())
            add(where.toString())
        }

        val query = entityManager.createQuery(sql.toString(), String::class.java)
        query.setParameters(params)

        return query.getResultList(String::class.java)
    }
}