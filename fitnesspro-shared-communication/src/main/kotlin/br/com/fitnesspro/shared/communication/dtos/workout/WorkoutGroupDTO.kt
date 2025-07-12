package br.com.fitnesspro.shared.communication.dtos.workout

import br.com.fitnesspro.shared.communication.dtos.common.AuditableDTO
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.DayOfWeek
import java.time.LocalDateTime

@Schema(description = "Classe DTO usada para manutenção de um grupo muscular do treino")
data class WorkoutGroupDTO(
    @Schema(description = "Identificador", example = "e874d31c-0e29-4e9b-b48e-7d70d91b6a16", required = false)
    @field:Size(min = 1, max = 255, message = "{baseDTO.id.size}")
    override var id: String? = null,

    @Schema(description = "Data de criação", example = "2023-01-01T10:00:00", required = false, readOnly = true)
    override var creationDate: LocalDateTime? = null,

    @Schema(description = "Data de atualização", example = "2023-01-01T10:00:00", required = false, readOnly = true)
    override var updateDate: LocalDateTime? = null,

    @Schema(description = "Valor booleano que representa se o registro está ativo", required = true)
    @field:NotNull(message = "{workoutGroupDTO.active.notNull}")
    var active: Boolean = true,

    @Schema(description = "Nome do grupo de treino", example = "Treino A", required = true)
    @field:Size(max = 255, message = "{workoutGroupDTO.name.size}")
    var name: String? = null,

    @Schema(description = "Identificador do treino", example = "e874d31c-0e29-4e9b-b48e-7d70d91b6a16", required = true)
    @field:NotNull(message = "{workoutGroupDTO.workoutId.notNull}")
    var workoutId: String? = null,

    @Schema(description = "Dia da semana", example = "MONDAY", required = true)
    @field:NotNull(message = "{workoutGroupDTO.dayWeek.notNull}")
    var dayWeek: DayOfWeek? = null,

    @Schema(description = "Ordem que o grupo vai aparecer na lista", example = "1", required = true)
    @field:NotNull(message = "{workoutGroupDTO.groupOrder.notNull}")
    var groupOrder: Int = 1,
): AuditableDTO