package br.com.fitnesspro.common.repository.jpa.report

import br.com.fitnesspro.jpa.helper.Constants.QR_NL
import br.com.fitnesspro.jpa.query.Parameter
import br.com.fitnesspro.jpa.query.getResultList
import br.com.fitnesspro.jpa.query.setParameters
import br.com.fitnesspro.models.general.Report
import br.com.fitnesspro.models.general.SchedulerReport
import br.com.fitnesspro.shared.communication.enums.report.EnumReportContext
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.ReportImportFilter
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class CustomReportRepositoryImpl: ICustomReportRepository {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    override fun getReportsImport(filter: ReportImportFilter, pageInfos: ImportPageInfos): List<Report> {
        val params = mutableListOf<Parameter>()

        val select = StringJoiner(QR_NL).apply {
            add(" select report ")
        }

        val from = StringJoiner(QR_NL).apply {
            when (filter.reportContext) {
                EnumReportContext.SCHEDULERS_REPORT -> {
                    add(" from ${SchedulerReport::class.java.name} intermediate ")
                }
            }

            add(" inner join intermediate.report report ")
        }

        val where = StringJoiner(QR_NL).apply {
            add(" where intermediate.person.id = :pPersonId ")

            params.add(Parameter(name = "pPersonId", value = filter.personId))

            filter.lastUpdateDateMap[Report::class.simpleName!!]?.let {
                add(" and report.updateDate > :pLastUpdateDate ")
                params.add(Parameter(name = "pLastUpdateDate", value = it))
            }
        }

        val orderBy = StringJoiner(QR_NL).apply {
            add(" order by report.updateDate asc, report.id asc ")
        }

        val sql = StringJoiner(QR_NL).apply {
            add(select.toString())
            add(from.toString())
            add(where.toString())
            add(orderBy.toString())
        }

        val query = entityManager.createQuery(sql.toString(), Report::class.java)
        query.setParameters(params)
        query.maxResults = pageInfos.pageSize

        val result = query.getResultList(Report::class.java)

        return result
    }

}