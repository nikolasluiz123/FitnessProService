package br.com.fitnesspro.shared.communication.query.filter.importation

import br.com.fitnesspro.core.enums.EnumDateTimePatterns
import br.com.fitnesspro.core.extensions.dateTimeNow
import br.com.fitnesspro.core.extensions.format
import java.time.LocalDateTime

open class CommonImportFilter(
    val lastUpdateDate: LocalDateTime? = null,
) {
    @Suppress(names = ["unused"])
    fun toCacheKey(): String {
        return lastUpdateDate?.format(EnumDateTimePatterns.DATE_TIME_SQLITE) ?: dateTimeNow().format(
            EnumDateTimePatterns.DATE_TIME_SQLITE)
    }
}