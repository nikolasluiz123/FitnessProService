package br.com.fitnesspro.service.communication.dtos.serviceauth

import br.com.fitnesspro.shared.communication.dtos.serviceauth.interfaces.IServiceTokenGenerationDTO
import br.com.fitnesspro.shared.communication.enums.serviceauth.EnumTokenType
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull

@Schema(description = "Classe DTO usada para geração de um token")
data class ValidatedServiceTokenGenerationDTO(
    @field:Schema(description = "Tipo de token", required = true)
    @field:NotNull(message = "serviceTokenGenerationDTO.type.notNull")
    override var type: EnumTokenType? = null,

    @field:Schema(description = "Identificador do usuário a qual o token pertence", required = false)
    override var userId: String? = null,

    @field:Schema(description = "Identificador do dispositivo a qual o token pertence", required = false)
    override var deviceId: String? = null,

    @field:Schema(description = "Identificador da aplicação a qual o token pertence", required = false)
    override var applicationId: String? = null
) : IServiceTokenGenerationDTO
