package br.com.fitnesspro.shared.communication.query.filter

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

open class CommonImportFilter(
    @Schema(
        description = "Data da última atualização, é usada como data de corte, ou seja, somente o dados posteriores a essa data serão importados",
        example = "2023-01-01T10:00:00",
        required = false
    )
    val lastUpdateDate: LocalDateTime? = null,
)