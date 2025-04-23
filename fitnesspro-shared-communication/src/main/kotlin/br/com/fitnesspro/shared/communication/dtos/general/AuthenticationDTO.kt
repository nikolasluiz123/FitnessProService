package br.com.fitnesspro.shared.communication.dtos.general

import br.com.fitnesspro.shared.communication.dtos.serviceauth.DeviceDTO
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class AuthenticationDTO(
    @Schema(description = "E-mail do usuário", example = "usuario@example.com", required = true)
    @field:NotBlank(message = "O e-mail é obrigatório")
    @field:Size(min = 1, max = 64, message = "O e-mail deve ter entre 1 e 64 caracteres")
    var email: String? = null,

    @Schema(description = "Senha do usuário", example = "senha123", required = true)
    @field:NotBlank(message = "A senha é obrigatória")
    @field:Size(min = 1, max = 1024, message = "A senha deve ter entre 1 e 1024 caracteres")
    var password: String? = null,

    @Schema(description = "Indica se a autenticação é de um administrador. Isso implica em validações internas")
    @field:NotNull(message = "O campo adminAuth é obrigatório")
    var adminAuth: Boolean = false,

    @Schema(description = "DTO do dispositivo que está autenticando")
    var deviceDTO: DeviceDTO? = null,

    @Schema(description = "Token JWT da aplicação que está fazendo a chamada")
    var applicationJWT: String? = null
)