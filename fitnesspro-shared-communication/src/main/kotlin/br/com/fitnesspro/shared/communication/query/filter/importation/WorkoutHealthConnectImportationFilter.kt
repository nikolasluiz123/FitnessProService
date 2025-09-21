package br.com.fitnesspro.shared.communication.query.filter.importation

import java.time.LocalDateTime

class WorkoutHealthConnectImportationFilter(
    val exerciseExecutionIds: List<String>,
    lastUpdateDateMap: Map<String, LocalDateTime?> = emptyMap()
): CommonImportFilter(lastUpdateDateMap)