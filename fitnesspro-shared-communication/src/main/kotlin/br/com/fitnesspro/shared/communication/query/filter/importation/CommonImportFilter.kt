package br.com.fitnesspro.shared.communication.query.filter.importation

import java.time.LocalDateTime

open class CommonImportFilter(
    val lastUpdateDateMap: Map<String, LocalDateTime?> = emptyMap(),
)