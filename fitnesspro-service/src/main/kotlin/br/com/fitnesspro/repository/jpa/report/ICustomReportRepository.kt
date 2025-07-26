package br.com.fitnesspro.repository.jpa.report

import br.com.fitnesspro.models.general.Report
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.ReportImportFilter

interface ICustomReportRepository {
    fun getReportsImport(filter: ReportImportFilter, pageInfos: ImportPageInfos): List<Report>
}