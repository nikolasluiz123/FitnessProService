package br.com.fitnesspro.services.general

import br.com.fitnesspro.config.application.cache.REPORT_IMPORT_CACHE_NAME
import br.com.fitnesspro.config.application.cache.SCHEDULER_REPORT_IMPORT_CACHE_NAME
import br.com.fitnesspro.repository.auditable.report.IReportRepository
import br.com.fitnesspro.repository.auditable.report.ISchedulerReportRepository
import br.com.fitnesspro.repository.jpa.report.ICustomReportRepository
import br.com.fitnesspro.repository.jpa.report.ICustomSchedulerReportRepository
import br.com.fitnesspro.services.mappers.ReportServiceMapper
import br.com.fitnesspro.shared.communication.dtos.general.ReportDTO
import br.com.fitnesspro.shared.communication.dtos.general.SchedulerReportDTO
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
    @CacheEvict(cacheNames = [SCHEDULER_REPORT_IMPORT_CACHE_NAME], allEntries = true)
    fun saveSchedulerReportBatch(schedulerReportDTOList: List<SchedulerReportDTO>) {
        val schedulerReports = schedulerReportDTOList.map {
            reportServiceMapper.getSchedulerReport(it)
        }

        schedulerReportRepository.saveAll(schedulerReports)
    }

    @CacheEvict(cacheNames = [REPORT_IMPORT_CACHE_NAME], allEntries = true)
    fun saveReportBatch(reportDTOList: List<ReportDTO>) {
        val reports = reportDTOList.map {
            reportServiceMapper.getReport(it)
        }

        reportRepository.saveAll(reports)
    }

    @Cacheable(cacheNames = [REPORT_IMPORT_CACHE_NAME], key = "#filter.toCacheKey()")
    fun getReportsImport(filter: ReportImportFilter, pageInfos: ImportPageInfos): List<ReportDTO> {
        return customReportRepository.getReportsImport(filter, pageInfos).map(reportServiceMapper::getReportDTO)
    }

    @Cacheable(cacheNames = [SCHEDULER_REPORT_IMPORT_CACHE_NAME], key = "#filter.toCacheKey()")
    fun getSchedulerReportsImport(filter: SchedulerReportImportFilter, pageInfos: ImportPageInfos): List<SchedulerReportDTO> {
        return customSchedulerReportRepository.getSchedulerReportsImport(filter, pageInfos).map(reportServiceMapper::getSchedulerReportDTO)
    }
}