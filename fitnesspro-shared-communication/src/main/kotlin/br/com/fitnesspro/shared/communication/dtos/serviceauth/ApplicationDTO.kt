package br.com.fitnesspro.shared.communication.dtos.serviceauth

import br.com.fitnesspro.shared.communication.dtos.common.BaseDTO
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

@Schema(description = "Classe DTO usada para manutenção de uma aplicação")
data class ApplicationDTO(
    @Schema(description = "Identificador", example = "e874d31c-0e29-4e9b-b48e-7d70d91b6a16", required = false)
    override var id: String? = null,

    @Schema(description = "Nome da aplicação", required = true)
    @field:NotBlank(message = "{applicationDTO.name.notBlank}")
    @field:Size(max = 255, message = "{applicationDTO.name.size}")
    var name: String? = null,

    @Schema(description = "Valor booleano que representa se o registro está ativo", required = true)
    @field:NotNull(message = "{applicationDTO.active.notNull}")
    var active: Boolean = true,
): BaseDTO