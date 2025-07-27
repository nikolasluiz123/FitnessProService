package br.com.fitnesspro.shared.communication.dtos.serviceauth

import br.com.fitnesspro.shared.communication.dtos.common.BaseDTO
import br.com.fitnesspro.shared.communication.dtos.general.UserDTO
import br.com.fitnesspro.shared.communication.enums.serviceauth.EnumTokenType
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "Classe DTO usada para leitura de um token")
data class ServiceTokenDTO(
    @field:Schema(description = "Identificador", example = "e874d31c-0e29-4e9b-b48e-7d70d91b6a16", required = false, readOnly = true)
    override var id: String? = null,

    @field:Schema(description = "Token JWT", required = true, readOnly = true)
    val jwtToken: String? = null,

    @field:Schema(description = "Tipo de token", required = true, readOnly = true)
    var type: EnumTokenType? = null,

    @field:Schema(description = "Data de criação", example = "2023-01-01T10:00:00", required = true, readOnly = true)
    val creationDate: LocalDateTime? = null,

    @field:Schema(description = "Data de expiração", example = "2023-01-01T10:00:00", required = false, readOnly = true)
    val expirationDate: LocalDateTime? = null,

    @field:Schema(description = "DTO do usuário a qual o token pertence", required = false, readOnly = true)
    var user: UserDTO? = null,

    @field:Schema(description = "DTO do dispositivo a qual o token pertence", required = false, readOnly = true)
    var device: DeviceDTO? = null,

    @field:Schema(description = "DTO da aplicação a qual o token pertence", required = false, readOnly = true)
    var application: ApplicationDTO? = null
) : BaseDTO
