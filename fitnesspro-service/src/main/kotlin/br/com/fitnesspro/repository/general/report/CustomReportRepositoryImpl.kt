package br.com.fitnesspro.repository.general.report

import br.com.fitnesspro.models.general.Report
import br.com.fitnesspro.models.general.SchedulerReport
import br.com.fitnesspro.repository.common.helper.Constants.QR_NL
import br.com.fitnesspro.repository.common.query.Parameter
import br.com.fitnesspro.repository.common.query.getResultList
import br.com.fitnesspro.repository.common.query.setParameters
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.SchedulerReportImportFilter
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class CustomReportRepositoryImpl: ICustomReportRepository {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    override fun getReportsFromSchedulerImport(filter: SchedulerReportImportFilter, pageInfos: ImportPageInfos): List<Report> {
        val params = mutableListOf<Parameter>()

        val select = StringJoiner(QR_NL).apply {
            add(" select report ")
        }

        val from = StringJoiner(QR_NL).apply {
            add(" from ${SchedulerReport::class.java.name} sr ")
            add(" inner join sr.report report ")
        }

        val where = StringJoiner(QR_NL).apply {
            add(" where sr.person.id = :pPersonId ")

            params.add(Parameter(name = "pPersonId", value = filter.personId))

            filter.lastUpdateDate?.let {
                add(" and report.updateDate >= :pLastUpdateDate ")
                params.add(Parameter(name = "pLastUpdateDate", value = it))
            }
        }

        val sql = StringJoiner(QR_NL).apply {
            add(select.toString())
            add(from.toString())
            add(where.toString())
        }

        val query = entityManager.createQuery(sql.toString(), Report::class.java)
        query.setParameters(params)
        query.firstResult = pageInfos.pageSize * pageInfos.pageNumber
        query.maxResults = pageInfos.pageSize

        val result = query.getResultList(Report::class.java)

        return result
    }

}