package br.com.fitnesspro.scheduler.service

import br.com.fitnesspro.common.service.general.ReportService
import br.com.fitnesspro.service.communication.dtos.sync.ValidatedSchedulerModuleSyncDTO
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.ReportImportFilter
import br.com.fitnesspro.shared.communication.query.filter.importation.SchedulerModuleImportationFilter
import br.com.fitnesspro.shared.communication.query.filter.importation.SchedulerReportImportFilter
import org.springframework.stereotype.Service

@Service
class SchedulerModuleSyncService(
    private val schedulerService: SchedulerService,
    private val reportService: ReportService,
    private val schedulerReportService: SchedulerReportService
) {

    fun getImportationData(filter: SchedulerModuleImportationFilter, pageInfos: ImportPageInfos): ValidatedSchedulerModuleSyncDTO {
        val reportImportFilter = ReportImportFilter(filter.personId, filter.reportContext, filter.lastUpdateDate)
        val schedulerReportImportFilter = SchedulerReportImportFilter(filter.personId, filter.lastUpdateDate)

        return ValidatedSchedulerModuleSyncDTO(
            schedulers = schedulerService.getSchedulesImport(filter, pageInfos),
            reports = reportService.getReportsImport(reportImportFilter, pageInfos),
            schedulerReports = schedulerReportService.getSchedulerReportsImport(schedulerReportImportFilter, pageInfos)
        )
    }

    fun saveExportedData(syncDTO: ValidatedSchedulerModuleSyncDTO) {
        schedulerService.saveSchedulerBatch(syncDTO.schedulers)
        reportService.saveReportBatch(syncDTO.reports)
        schedulerReportService.saveSchedulerReportBatch(syncDTO.schedulerReports)
    }
}