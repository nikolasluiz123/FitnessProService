package br.com.fitnesspro.repository.scheduler

import br.com.fitnesspro.models.scheduler.SchedulerConfig
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.CommonImportFilter


interface ICustomSchedulerConfigRepository {

    fun getSchedulerConfigImport(filter: CommonImportFilter, pageInfos: ImportPageInfos): List<SchedulerConfig>
}