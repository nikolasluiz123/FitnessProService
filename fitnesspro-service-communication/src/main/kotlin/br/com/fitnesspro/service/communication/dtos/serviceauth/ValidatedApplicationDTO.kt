package br.com.fitnesspro.service.communication.dtos.serviceauth

import br.com.fitnesspro.shared.communication.dtos.serviceauth.interfaces.IApplicationDTO
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

@Schema(description = "Classe DTO usada para validação de uma aplicação")
data class ValidatedApplicationDTO(
    @field:Schema(description = "Identificador", example = "e874d31c-0e29-4e9b-b48e-7d70d91b6a16", required = false)
    @field:Size(min = 1, max = 255, message = "baseDTO.id.size")
    override var id: String? = null,

    @field:Schema(description = "Nome da aplicação", example = "FitnessPro Mobile", required = true)
    @field:NotBlank(message = "applicationDTO.name.notBlank")
    override val name: String? = null,

    @field:Schema(description = "Valor booleano que representa se o registro está ativo", required = true)
    @field:NotNull(message = "applicationDTO.active.notNull")
    override var active: Boolean = true,
) : IApplicationDTO