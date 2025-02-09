package br.com.fitnesspro.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class AcademyDTO(
    @Schema(description = "Identificador", example = "e874d31c-0e29-4e9b-b48e-7d70d91b6a16", required = false)
    @field:Size(min = 1, max = 255, message = "O identificador deve ter entre 1 e 255 caracteres")
    val id: String? = null,

    @Schema(description = "Nome da academia", example = "Academia XYZ", required = true)
    @field:Size(min = 1, max = 512, message = "O nome deve ter entre 1 e 512 caracteres")
    var name: String? = null,

    @Schema(description = "Endereço da academia", example = "Rua Exemplo, 123", required = true)
    @field:Size(min = 1, max = 512, message = "O endereço deve ter entre 1 e 512 caracteres")
    var address: String? = null,

    @Schema(description = "Telefone da academia", example = "1112345678", required = true)
    @field:Size(min = 1, max = 11, message = "O telefone deve ter entre 1 e 11 caracteres")
    var phone: String? = null,

    @Schema(description = "Valor booleano que representa se o registro está ativo", required = true)
    @field:NotNull(message = "O campo ativo é obrigatório")
    var active: Boolean = true
)