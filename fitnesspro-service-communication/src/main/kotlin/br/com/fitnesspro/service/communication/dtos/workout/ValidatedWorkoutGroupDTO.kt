package br.com.fitnesspro.service.communication.dtos.workout

import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.IWorkoutGroupDTO
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.DayOfWeek
import java.time.LocalDateTime

@Schema(description = "Classe DTO usada para manutenção de um grupo muscular do treino")
data class ValidatedWorkoutGroupDTO(
    @field:Schema(description = "Identificador", example = "e874d31c-0e29-4e9b-b48e-7d70d91b6a16", required = false)
    @field:Size(min = 1, max = 255, message = "baseDTO.id.size")
    override var id: String? = null,

    @field:Schema(description = "Data de criação", example = "2023-01-01T10:00:00", required = false, readOnly = true)
    override var creationDate: LocalDateTime? = null,

    @field:Schema(description = "Data de atualização", example = "2023-01-01T10:00:00", required = false, readOnly = true)
    override var updateDate: LocalDateTime? = null,

    @field:Schema(description = "Valor booleano que representa se o registro está ativo", required = true)
    @field:NotNull(message = "workoutGroupDTO.active.notNull")
    override var active: Boolean = true,

    @field:Schema(description = "Nome do grupo de treino", example = "Treino A", required = true)
    @field:NotEmpty(message = "workoutGroupDTO.name.notBlank")
    @field:Size(max = 255, message = "workoutGroupDTO.name.size")
    override var name: String? = null,

    @field:Schema(description = "Identificador do treino", example = "e874d31c-0e29-4e9b-b48e-7d70d91b6a16", required = true)
    @field:NotNull(message = "workoutGroupDTO.workoutId.notNull")
    @field:Size(min = 1, max = 255, message = "baseDTO.id.size")
    override var workoutId: String? = null,

    @field:Schema(description = "Dia da semana", example = "MONDAY", required = true)
    @field:NotNull(message = "workoutGroupDTO.dayWeek.notNull")
    override var dayWeek: DayOfWeek? = null,

    @field:Schema(description = "Ordem que o grupo vai aparecer na lista", example = "1", required = true)
    @field:NotNull(message = "workoutGroupDTO.groupOrder.notNull")
    override var groupOrder: Int = 1,
): IWorkoutGroupDTO