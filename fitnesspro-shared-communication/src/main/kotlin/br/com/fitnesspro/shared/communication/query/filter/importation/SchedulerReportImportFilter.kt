package br.com.fitnesspro.shared.communication.query.filter.importation

import java.time.LocalDateTime

class SchedulerReportImportFilter(
    val personId: String,
    lastUpdateDate: LocalDateTime? = null
): CommonImportFilter(lastUpdateDate)