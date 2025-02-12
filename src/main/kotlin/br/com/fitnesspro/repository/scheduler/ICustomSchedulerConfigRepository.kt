package br.com.fitnesspro.repository.scheduler

import br.com.fitnesspro.dto.scheduler.SchedulerConfigDTO
import br.com.fitnesspro.repository.common.filter.CommonImportFilter
import br.com.fitnesspro.repository.common.paging.ImportPageInfos

interface ICustomSchedulerConfigRepository {

    fun getSchedulerConfigImport(filter: CommonImportFilter, pageInfos: ImportPageInfos): List<SchedulerConfigDTO>
}