package br.com.fitnesspro.common.service.general

import br.com.fitnesspro.core.cache.REPORT_IMPORT_CACHE_NAME
import br.com.fitnesspro.common.repository.auditable.report.IReportRepository
import br.com.fitnesspro.common.repository.jpa.report.ICustomReportRepository
import br.com.fitnesspro.common.service.mappers.ReportServiceMapper
import br.com.fitnesspro.service.communication.dtos.general.ValidatedReportDTO
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.ReportImportFilter
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class ReportService(
    private val reportRepository: IReportRepository,
    private val customReportRepository: ICustomReportRepository,
    private val reportServiceMapper: ReportServiceMapper,
) {
    @CacheEvict(cacheNames = [REPORT_IMPORT_CACHE_NAME], allEntries = true)
    fun saveReportBatch(validatedReportDTOList: List<ValidatedReportDTO>) {
        val reports = validatedReportDTOList.map {
            reportServiceMapper.getReport(it)
        }

        reportRepository.saveAll(reports)
    }

    @Cacheable(cacheNames = [REPORT_IMPORT_CACHE_NAME], key = "#filter.toCacheKey()")
    fun getReportsImport(filter: ReportImportFilter, pageInfos: ImportPageInfos): List<ValidatedReportDTO> {
        return customReportRepository.getReportsImport(filter, pageInfos).map(reportServiceMapper::getValidatedReportDTO)
    }
}