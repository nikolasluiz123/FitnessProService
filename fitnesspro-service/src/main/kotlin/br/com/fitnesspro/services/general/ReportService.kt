package br.com.fitnesspro.services.general

import br.com.fitnesspro.config.application.cache.REPORT_IMPORT_CACHE_NAME
import br.com.fitnesspro.config.application.cache.SCHEDULER_REPORT_IMPORT_CACHE_NAME
import br.com.fitnesspro.models.general.Report
import br.com.fitnesspro.models.general.SchedulerReport
import br.com.fitnesspro.repository.jpa.report.ICustomReportRepository
import br.com.fitnesspro.repository.jpa.report.ICustomSchedulerReportRepository
import br.com.fitnesspro.repository.auditable.report.IReportRepository
import br.com.fitnesspro.repository.auditable.report.ISchedulerReportRepository
import br.com.fitnesspro.services.mappers.ReportServiceMapper
import br.com.fitnesspro.shared.communication.dtos.general.ReportDTO
import br.com.fitnesspro.shared.communication.dtos.general.SchedulerReportDTO
import br.com.fitnesspro.shared.communication.enums.report.EnumReportContext
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.ReportImportFilter
import br.com.fitnesspro.shared.communication.query.filter.importation.SchedulerReportImportFilter
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class ReportService(
    private val reportRepository: IReportRepository,
    private val customReportRepository: ICustomReportRepository,
    private val schedulerReportRepository: ISchedulerReportRepository,
    private val customSchedulerReportRepository: ICustomSchedulerReportRepository,
    private val reportServiceMapper: ReportServiceMapper,
) {
    @CacheEvict(cacheNames = [SCHEDULER_REPORT_IMPORT_CACHE_NAME, REPORT_IMPORT_CACHE_NAME], allEntries = true)
    fun saveSchedulerReport(schedulerReportDTO: SchedulerReportDTO) {
        val report = reportServiceMapper.getReport(schedulerReportDTO.report!!)
        reportRepository.save(report)

        schedulerReportDTO.report?.id = report.id
        val schedulerReport = reportServiceMapper.getSchedulerReport(schedulerReportDTO)
        schedulerReportRepository.save(schedulerReport)
    }

    @CacheEvict(cacheNames = [SCHEDULER_REPORT_IMPORT_CACHE_NAME, REPORT_IMPORT_CACHE_NAME], allEntries = true)
    fun saveSchedulerReportBatch(schedulerReportDTOList: List<SchedulerReportDTO>) {
        val reports = mutableListOf<Report>()
        val schedulerReports = mutableListOf<SchedulerReport>()

        schedulerReportDTOList.forEach {
            val report = reportServiceMapper.getReport(it.report!!)
            reports.add(report)

            val schedulerReport = reportServiceMapper.getSchedulerReport(it)
            schedulerReports.add(schedulerReport)
        }

        reportRepository.saveAll(reports)
        schedulerReportRepository.saveAll(schedulerReports)
    }

    @Cacheable(cacheNames = [REPORT_IMPORT_CACHE_NAME], key = "#filter.toCacheKey()")
    fun getReportsImport(filter: ReportImportFilter, pageInfos: ImportPageInfos): List<ReportDTO> {
        return customReportRepository.getReportsImport(filter, pageInfos).map(reportServiceMapper::getReportDTO)
    }

    @Cacheable(cacheNames = [SCHEDULER_REPORT_IMPORT_CACHE_NAME], key = "#filter.toCacheKey()")
    fun getSchedulerReportsImport(filter: SchedulerReportImportFilter, pageInfos: ImportPageInfos): List<SchedulerReportDTO> {
        return customSchedulerReportRepository.getSchedulerReportsImport(filter, pageInfos).map(reportServiceMapper::getSchedulerReportDTO)
    }

    fun deleteSchedulerReport(reportId: String, authenticatedUserId: String) {
        val schedulerReportsIds = customSchedulerReportRepository.getSchedulerReportIdsDelete(
            context = EnumReportContext.SCHEDULERS_REPORT,
            authenticatedUserId = authenticatedUserId,
            reportId = reportId
        )

        schedulerReportRepository.deleteAllById(schedulerReportsIds)
        reportRepository.deleteById(reportId)
    }

    fun deleteAllSchedulerReport(authenticatedUserId: String) {
        val reportsIds = customSchedulerReportRepository.getReportIdsFromSchedulerDelete(
            context = EnumReportContext.SCHEDULERS_REPORT,
            personId = authenticatedUserId,
        )

        val schedulerReportsIds = customSchedulerReportRepository.getSchedulerReportIdsDelete(
            context = EnumReportContext.SCHEDULERS_REPORT,
            authenticatedUserId = authenticatedUserId,
        )

        schedulerReportRepository.deleteAllById(schedulerReportsIds)
        reportRepository.deleteAllById(reportsIds)
    }
}