package br.com.fitnesspro.service.repository.scheduler

import br.com.fitnesspro.shared.communication.filter.CommonImportFilter
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.dtos.scheduler.SchedulerConfigDTO


interface ICustomSchedulerConfigRepository {

    fun getSchedulerConfigImport(filter: CommonImportFilter, pageInfos: ImportPageInfos): List<SchedulerConfigDTO>
}