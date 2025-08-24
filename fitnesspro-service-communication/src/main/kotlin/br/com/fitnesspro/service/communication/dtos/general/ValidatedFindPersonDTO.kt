package br.com.fitnesspro.service.communication.dtos.general

import br.com.fitnesspro.shared.communication.dtos.general.interfaces.IFindPersonDTO
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Schema(description = "Classe DTO usada para buscar uma pessoa com os parâmetros informados")
data class ValidatedFindPersonDTO(
    @field:Schema(description = "E-mail do usuário", example = "usuario@example.com", required = true)
    @field:NotBlank(message = "authenticationDTO.email.notBlank")
    @field:Size(min = 1, max = 64, message = "authenticationDTO.email.size")
    override var email: String? = null,

    @field:Schema(description = "Senha do usuário", example = "senha123", required = true)
    override var password: String? = null,
): IFindPersonDTO