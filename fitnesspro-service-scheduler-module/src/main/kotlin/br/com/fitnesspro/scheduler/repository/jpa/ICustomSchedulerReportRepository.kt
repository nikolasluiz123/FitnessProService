package br.com.fitnesspro.scheduler.repository.jpa

import br.com.fitnesspro.models.general.SchedulerReport
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.SchedulerReportImportFilter

interface ICustomSchedulerReportRepository {

    fun getSchedulerReportsImport(
        filter: SchedulerReportImportFilter,
        pageInfos: ImportPageInfos
    ): List<SchedulerReport>

}