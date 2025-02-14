package br.com.fitnesspro.service.repository.scheduler

import br.com.fitnesspro.shared.communication.dtos.scheduler.SchedulerConfigDTO
import br.com.fitnesspro.service.repository.common.filter.CommonImportFilter
import br.com.fitnesspro.service.repository.common.paging.ImportPageInfos


interface ICustomSchedulerConfigRepository {

    fun getSchedulerConfigImport(filter: CommonImportFilter, pageInfos: ImportPageInfos): List<SchedulerConfigDTO>
}