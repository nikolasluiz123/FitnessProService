package br.com.fitnesspro.shared.communication.query.filter.importation

import br.com.fitnesspro.core.enums.EnumDateTimePatterns
import br.com.fitnesspro.core.extensions.dateTimeNow
import br.com.fitnesspro.core.extensions.format
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

open class CommonImportFilter(
    @Schema(
        description = "Data da última atualização, é usada como data de corte, ou seja, somente o dados posteriores a essa data serão importados",
        example = "2023-01-01T10:00:00",
        required = false
    )
    val lastUpdateDate: LocalDateTime? = null,
) {
    @Suppress(names = ["unused"])
    fun toCacheKey(): String {
        return lastUpdateDate?.format(EnumDateTimePatterns.DATE_TIME_SQLITE) ?: dateTimeNow().format(
            EnumDateTimePatterns.DATE_TIME_SQLITE)
    }
}