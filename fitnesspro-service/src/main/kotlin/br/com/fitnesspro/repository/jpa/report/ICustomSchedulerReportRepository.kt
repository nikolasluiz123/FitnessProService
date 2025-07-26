package br.com.fitnesspro.repository.jpa.report

import br.com.fitnesspro.models.general.SchedulerReport
import br.com.fitnesspro.shared.communication.enums.report.EnumReportContext
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.SchedulerReportImportFilter

interface ICustomSchedulerReportRepository {

    fun getSchedulerReportsImport(filter: SchedulerReportImportFilter, pageInfos: ImportPageInfos): List<SchedulerReport>

    fun getReportIdsFromSchedulerDelete(context: EnumReportContext, personId: String): List<String>

    fun getSchedulerReportIdsDelete(context: EnumReportContext, authenticatedUserId: String, reportId: String? = null): List<String>
}