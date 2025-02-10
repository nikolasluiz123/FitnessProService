package br.com.fitnesspro.dto.scheduler

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class SchedulerConfigDTO(
    @Schema(description = "Identificador", example = "e874d31c-0e29-4e9b-b48e-7d70d91b6a16", required = false)
    @field:Size(min = 1, max = 255, message = "O identificador deve ter entre 1 e 255 caracteres")
    val id: String? = null,

    @Schema(description = "Valor booleano que representa se o registro está ativo", required = true)
    @field:NotNull(message = "O campo ativo é obrigatório")
    var active: Boolean = false,

    @Schema(description = "Valor booleano que representa se o alarme está ativado", required = true)
    @field:NotNull(message = "O campo alarme é obrigatório")
    var alarm: Boolean = false,

    @Schema(description = "Valor booleano que representa se a notificação está ativada", required = true)
    @field:NotNull(message = "O campo notificação é obrigatório")
    var notification: Boolean = false,

    @Schema(description = "Densidade mínima de agendamento", example = "1", required = true)
    @field:Min(value = 1, message = "A densidade mínima de agendamento deve ser no mínimo 1")
    var minScheduleDensity: Int = 1,

    @Schema(description = "Densidade máxima de agendamento", example = "2", required = true)
    @field:Min(value = 1, message = "A densidade máxima de agendamento deve ser no mínimo 1")
    var maxScheduleDensity: Int = 2,

    @Schema(description = "Identificador da pessoa", example = "e874d31c-0e29-4e9b-b48e-7d70d91b6a16", required = true)
    @field:NotNull(message = "O identificador da pessoa é obrigatório")
    var personId: String? = null
)