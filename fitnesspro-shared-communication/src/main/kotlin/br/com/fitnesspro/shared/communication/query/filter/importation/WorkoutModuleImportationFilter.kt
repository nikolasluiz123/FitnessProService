package br.com.fitnesspro.shared.communication.query.filter.importation

import java.time.LocalDateTime

class WorkoutModuleImportationFilter(
    val personId: String,
    lastUpdateDate: LocalDateTime? = null
): CommonImportFilter(lastUpdateDate)