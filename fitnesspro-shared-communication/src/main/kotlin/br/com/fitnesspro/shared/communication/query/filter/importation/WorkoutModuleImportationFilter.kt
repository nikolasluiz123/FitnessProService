package br.com.fitnesspro.shared.communication.query.filter.importation

import java.time.LocalDateTime

class WorkoutModuleImportationFilter(
    val personId: String,
    lastUpdateDateMap: Map<String, LocalDateTime?> = emptyMap()
): CommonImportFilter(lastUpdateDateMap)