package br.com.fitnesspro.shared.communication.dtos.serviceauth

import br.com.fitnesspro.shared.communication.enums.serviceauth.EnumTokenType
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull

data class ServiceTokenGenerationDTO(
    @Schema(description = "Tipo de token", required = true)
    @field:NotNull(message = "O tipo do token é obrigatório")
    val type: EnumTokenType? = null,

    @Schema(description = "Identificador do usuário a qual o token pertence", required = false)
    val userId: String? = null,

    @Schema(description = "Identificador do dispositivo a qual o token pertence", required = false)
    val deviceId: String? = null,

    @Schema(description = "Identificador da aplicação a qual o token pertence", required = false)
    val applicationId: String? = null
)
