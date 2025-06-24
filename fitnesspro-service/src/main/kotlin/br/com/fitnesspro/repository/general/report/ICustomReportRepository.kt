package br.com.fitnesspro.repository.general.report

import br.com.fitnesspro.models.general.Report
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.SchedulerReportImportFilter

interface ICustomReportRepository {
    fun getReportsFromSchedulerImport(filter: SchedulerReportImportFilter, pageInfos: ImportPageInfos): List<Report>
}