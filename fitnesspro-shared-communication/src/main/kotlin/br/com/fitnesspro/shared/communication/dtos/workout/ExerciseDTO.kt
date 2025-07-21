package br.com.fitnesspro.shared.communication.dtos.workout

import br.com.fitnesspro.shared.communication.dtos.common.AuditableDTO
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

@Schema(description = "Classe DTO usada para manutenção dos exercícios do treino")
data class ExerciseDTO(
    @Schema(description = "Identificador", example = "e874d31c-0e29-4e9b-b48e-7d70d91b6a16", required = false)
    @field:Size(min = 1, max = 255, message = "baseDTO.id.size")
    override var id: String? = null,

    @Schema(description = "Data de criação", example = "2023-01-01T10:00:00", required = false, readOnly = true)
    override var creationDate: LocalDateTime? = null,

    @Schema(description = "Data de atualização", example = "2023-01-01T10:00:00", required = false, readOnly = true)
    override var updateDate: LocalDateTime? = null,

    @Schema(description = "Valor booleano que representa se o registro está ativo", required = true)
    @field:NotNull(message = "exerciseDTO.active.notNull")
    var active: Boolean = true,

    @Schema(description = "Nome do exercício", example = "Supino Reto", required = true)
    @field:NotNull(message = "exerciseDTO.name.notNull")
    @field:Size(max = 255, message = "exerciseDTO.name.size")
    @field:NotEmpty(message = "exerciseDTO.name.notNull")
    var name: String? = null,

    @Schema(description = "Duração do exercício definida em milisegundos", example = "120000", required = false)
    @field:Min(value = 1, message = "exerciseDTO.duration.min")
    var duration: Long? = null,

    @Schema(description = "Quantidade de repetições que devem ser realizadas em cada série", example = "12", required = false)
    @field:Min(value = 1, message = "exerciseDTO.repetitions.min")
    var repetitions: Int? = null,

    @Schema(description = "Quantidade de séries que devem ser realizadas", example = "3", required = false)
    @field:Min(value = 1, message = "exerciseDTO.sets.min")
    var sets: Int? = null,

    @Schema(description = "Quantidade de descanso definida em milisegundos", example = "30000", required = false)
    @field:Min(value = 1, message = "exerciseDTO.rest.min")
    var rest: Long? = null,

    @Schema(description = "Observação", example = "Observação sobre o exercício", required = false)
    @field:Size(max = 4096, message = "exerciseDTO.observation.size")
    var observation: String? = null,

    @Schema(description = "Ordem do exercício no treino", example = "1", required = true)
    @field:NotNull(message = "exerciseDTO.exerciseOrder.notNull")
    @field:Min(value = 1, message = "exerciseDTO.exerciseOrder.min")
    var exerciseOrder: Int? = null,

    @Schema(description = "DTO para atualização das informações do Grupo Muscular do Exercício. É opcional pois há cenários onde não vamos processar o grupo, apenas o exercício.", required = false)
    var workoutGroupDTO: WorkoutGroupDTO? = null
) : AuditableDTO