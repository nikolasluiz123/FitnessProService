package br.com.fitnesspro.shared.communication.dtos.serviceauth

import br.com.fitnesspro.shared.communication.enums.serviceauth.EnumTokenType
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull

@Schema(description = "Classe DTO usada para geração de um token")
data class ServiceTokenGenerationDTO(
    @field:Schema(description = "Tipo de token", required = true)
    @field:NotNull(message = "serviceTokenGenerationDTO.type.notNull")
    var type: EnumTokenType? = null,

    @field:Schema(description = "Identificador do usuário a qual o token pertence", required = false)
    var userId: String? = null,

    @field:Schema(description = "Identificador do dispositivo a qual o token pertence", required = false)
    var deviceId: String? = null,

    @field:Schema(description = "Identificador da aplicação a qual o token pertence", required = false)
    var applicationId: String? = null
)
