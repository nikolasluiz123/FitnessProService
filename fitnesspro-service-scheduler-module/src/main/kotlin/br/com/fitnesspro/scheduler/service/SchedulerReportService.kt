package br.com.fitnesspro.scheduler.service

import br.com.fitnesspro.core.cache.SCHEDULER_REPORT_IMPORT_CACHE_NAME
import br.com.fitnesspro.scheduler.repository.auditable.ISchedulerReportRepository
import br.com.fitnesspro.scheduler.repository.jpa.ICustomSchedulerReportRepository
import br.com.fitnesspro.scheduler.service.mappers.SchedulerReportServiceMapper
import br.com.fitnesspro.shared.communication.dtos.general.SchedulerReportDTO
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.SchedulerReportImportFilter
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class SchedulerReportService(
    private val schedulerReportRepository: ISchedulerReportRepository,
    private val customSchedulerReportRepository: ICustomSchedulerReportRepository,
    private val schedulerReportServiceMapper: SchedulerReportServiceMapper,
) {
    @CacheEvict(cacheNames = [SCHEDULER_REPORT_IMPORT_CACHE_NAME], allEntries = true)
    fun saveSchedulerReportBatch(schedulerReportDTOList: List<SchedulerReportDTO>) {
        val schedulerReports = schedulerReportDTOList.map {
            schedulerReportServiceMapper.getSchedulerReport(it)
        }

        schedulerReportRepository.saveAll(schedulerReports)
    }

    @Cacheable(cacheNames = [SCHEDULER_REPORT_IMPORT_CACHE_NAME], key = "#filter.toCacheKey()")
    fun getSchedulerReportsImport(filter: SchedulerReportImportFilter, pageInfos: ImportPageInfos): List<SchedulerReportDTO> {
        return customSchedulerReportRepository.getSchedulerReportsImport(filter, pageInfos).map(schedulerReportServiceMapper::getSchedulerReportDTO)
    }
}