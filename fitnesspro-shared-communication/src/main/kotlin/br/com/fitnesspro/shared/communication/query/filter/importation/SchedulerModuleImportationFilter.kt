package br.com.fitnesspro.shared.communication.query.filter.importation

import br.com.fitnesspro.shared.communication.enums.report.EnumReportContext
import java.time.LocalDateTime

class SchedulerModuleImportationFilter(
    val personId: String,
    val reportContext: EnumReportContext,
    lastUpdateDate: LocalDateTime? = null
): CommonImportFilter(lastUpdateDate)