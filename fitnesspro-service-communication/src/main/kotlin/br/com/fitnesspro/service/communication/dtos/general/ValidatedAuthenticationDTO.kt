package br.com.fitnesspro.service.communication.dtos.general

import br.com.fitnesspro.shared.communication.dtos.general.interfaces.IAuthenticationDTO
import br.com.fitnesspro.shared.communication.dtos.serviceauth.interfaces.IDeviceDTO
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

@Schema(description = "Classe DTO usada para autenticação de um usuário")
data class ValidatedAuthenticationDTO(
    @field:Schema(description = "E-mail do usuário", example = "usuario@example.com", required = true)
    @field:NotBlank(message = "authenticationDTO.email.notBlank")
    @field:Size(min = 1, max = 64, message = "authenticationDTO.email.size")
    override var email: String? = null,

    @field:Schema(description = "Senha do usuário", example = "senha123", required = true)
    @field:NotBlank(message = "authenticationDTO.password.notBlank")
    @field:Size(min = 1, max = 1024, message = "authenticationDTO.password.size")
    override var password: String? = null,

    @field:Schema(description = "Indica se a autenticação é de um administrador. Isso implica em validações internas")
    @field:NotNull(message = "authenticationDTO.adminAuth.notNull")
    override var adminAuth: Boolean = false,

    @field:Schema(description = "DTO do dispositivo que está autenticando")
    override var deviceDTO: IDeviceDTO? = null,

    @field:Schema(description = "Token JWT da aplicação que está fazendo a chamada")
    @field:NotBlank(message = "authenticationDTO.applicationJWT.notBlank")
    override var applicationJWT: String? = null
): IAuthenticationDTO