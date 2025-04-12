package br.com.fitnesspro.shared.communication.dtos.serviceauth

import br.com.fitnesspro.shared.communication.dtos.common.BaseDTO
import br.com.fitnesspro.shared.communication.dtos.general.UserDTO
import br.com.fitnesspro.shared.communication.enums.serviceauth.EnumTokenType
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class ServiceTokenDTO(
    @Schema(description = "Identificador", example = "e874d31c-0e29-4e9b-b48e-7d70d91b6a16", required = false, readOnly = true)
    override var id: String? = null,

    @Schema(description = "Token JWT", required = true, readOnly = true)
    val jwtToken: String? = null,

    @Schema(description = "Tipo de token", required = true, readOnly = true)
    val type: EnumTokenType? = null,

    @Schema(description = "Data de criação", example = "2023-01-01T10:00:00", required = true, readOnly = true)
    val creationDate: LocalDateTime? = null,

    @Schema(description = "Data de expiração", example = "2023-01-01T10:00:00", required = false, readOnly = true)
    val expirationDate: LocalDateTime? = null,

    @Schema(description = "DTO do usuário a qual o token pertence", required = false, readOnly = true)
    val userDTO: UserDTO? = null,

    @Schema(description = "DTO do dispositivo a qual o token pertence", required = false, readOnly = true)
    val deviceDTO: DeviceDTO? = null,

    @Schema(description = "DTO da aplicação a qual o token pertence", required = false, readOnly = true)
    val applicationDTO: ApplicationDTO? = null
) : BaseDTO
