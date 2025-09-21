package br.com.fitnesspro.common.service.general

import br.com.fitnesspro.common.repository.auditable.report.IReportRepository
import br.com.fitnesspro.common.repository.jpa.report.ICustomReportRepository
import br.com.fitnesspro.common.service.mappers.ReportServiceMapper
import br.com.fitnesspro.common.service.storage.ReportGCBucketService
import br.com.fitnesspro.core.cache.REPORT_IMPORT_CACHE_NAME
import br.com.fitnesspro.service.communication.dtos.general.ValidatedReportDTO
import br.com.fitnesspro.shared.communication.dtos.general.interfaces.IReportDTO
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.ReportImportFilter
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import kotlin.collections.map

@Service
class ReportService(
    private val reportRepository: IReportRepository,
    private val customReportRepository: ICustomReportRepository,
    private val reportServiceMapper: ReportServiceMapper,
    private val reportGCBucketService: ReportGCBucketService,
) {
    fun saveReportBatch(list: List<IReportDTO>) {
        val reports = list.map {
            reportServiceMapper.getReport(it)
        }

        reportRepository.saveAll(reports)

        val inactiveReports = reports
            .filter { !it.active }
            .map { it.id }

        if (inactiveReports.isNotEmpty()) {
            reportGCBucketService.deleteReport(inactiveReports)
        }
    }

    fun getReportsImport(filter: ReportImportFilter, pageInfos: ImportPageInfos): List<ValidatedReportDTO> {
        return customReportRepository.getReportsImport(filter, pageInfos).map(reportServiceMapper::getValidatedReportDTO)
    }
}