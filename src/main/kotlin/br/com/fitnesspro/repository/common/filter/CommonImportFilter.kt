package br.com.fitnesspro.repository.common.filter

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

open class CommonImportFilter(
    @Schema(description = "Indica se apenas usuários ativos devem ser importados", required = true)
    @field:NotNull(message = "O campo 'onlyActives' é obrigatório")
    val onlyActives: Boolean = true,

    @Schema(
        description = "Data da última atualização, é usada como data de corte, ou seja, somente o dados posteriores a essa data serão importados",
        example = "2023-01-01T10:00:00",
        required = false
    )
    val lastUpdateDate: LocalDateTime? = null,
)