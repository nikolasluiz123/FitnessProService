package br.com.fitnesspro.shared.communication.dtos.workout

import br.com.fitnesspro.shared.communication.dtos.common.AuditableDTO
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.DayOfWeek
import java.time.LocalDateTime

data class WorkoutGroupDTO(
    @Schema(description = "Identificador", example = "e874d31c-0e29-4e9b-b48e-7d70d91b6a16", required = false)
    @field:Size(min = 1, max = 255, message = "O identificador deve ter entre 1 e 255 caracteres")
    override var id: String? = null,

    @Schema(description = "Data de criação", example = "2023-01-01T10:00:00", required = false, readOnly = true)
    override var creationDate: LocalDateTime? = null,

    @Schema(description = "Data de atualização", example = "2023-01-01T10:00:00", required = false, readOnly = true)
    override var updateDate: LocalDateTime? = null,

    @Schema(description = "Usuário que criou o registro", required = false)
    override var creationUserId: String? = null,

    @Schema(description = "Usuário que atualizou o registro", required = false)
    override var updateUserId: String? = null,

    @Schema(description = "Valor booleano que representa se o registro está ativo", required = true)
    @field:NotNull(message = "O campo ativo é obrigatório")
    var active: Boolean = true,

    @Schema(description = "Nome do grupo de treino", example = "Treino A", required = true)
    @field:NotNull(message = "O nome do grupo de treino é obrigatório")
    @field:Size(min = 1, max = 255, message = "O nome do grupo de treino deve ter entre 1 e 255 caracteres")
    var name: String? = null,

    @Schema(description = "Identificador do treino", example = "e874d31c-0e29-4e9b-b48e-7d70d91b6a16", required = true)
    @field:NotNull(message = "O identificador do treino é obrigatório")
    var workoutId: String? = null,

    @Schema(description = "Dia da semana", example = "MONDAY", required = true)
    @field:NotNull(message = "O dia da semana é obrigatório")
    var dayWeek: DayOfWeek? = null,
): AuditableDTO