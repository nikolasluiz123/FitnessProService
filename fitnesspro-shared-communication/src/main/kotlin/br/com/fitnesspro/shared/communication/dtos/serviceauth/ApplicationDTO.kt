package br.com.fitnesspro.shared.communication.dtos.serviceauth

import br.com.fitnesspro.shared.communication.dtos.common.BaseDTO
import io.swagger.v3.oas.annotations.media.Schema

data class ApplicationDTO(
    @Schema(description = "Identificador", example = "e874d31c-0e29-4e9b-b48e-7d70d91b6a16", required = false)
    override var id: String? = null,

    @Schema(description = "Nome da aplicação", required = true)
    val name: String? = null,

    @Schema(description = "Valor booleano que representa se o registro está ativo", required = true)
    var active: Boolean = true,
): BaseDTO