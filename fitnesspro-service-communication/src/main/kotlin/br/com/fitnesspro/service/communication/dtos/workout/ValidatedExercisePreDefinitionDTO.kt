package br.com.fitnesspro.service.communication.dtos.workout

import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.IExercisePreDefinitionDTO
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

@Schema(description = "Classe DTO usada para manutenção dos exercícios predefinidos")
data class ValidatedExercisePreDefinitionDTO(
    @field:Schema(description = "Identificador", example = "e874d31c-0e29-4e9b-b48e-7d70d91b6a16", required = false)
    @field:Size(min = 1, max = 255, message = "baseDTO.id.size")
    override var id: String? = null,

    @field:Schema(description = "Data de criação", example = "2023-01-01T10:00:00", required = false, readOnly = true)
    override var creationDate: LocalDateTime? = null,

    @field:Schema(description = "Data de atualização", example = "2023-01-01T10:00:00", required = false, readOnly = true)
    override var updateDate: LocalDateTime? = null,

    @field:Schema(description = "Valor booleano que representa se o registro está ativo", required = true)
    @field:NotNull(message = "exercisePreDefinitionDTO.active.notNull")
    override var active: Boolean = true,

    @field:Schema(description = "Nome do exercício", example = "Supino Reto", required = true)
    @field:NotNull(message = "exerciseDTO.name.notNull")
    @field:Size(max = 255, message = "exerciseDTO.name.size")
    @field:NotEmpty(message = "exerciseDTO.name.notNull")
    override var name: String? = null,

    @field:Schema(description = "Duração do exercício definida em milisegundos", example = "120000", required = false)
    override var duration: Long? = null,

    @field:Schema(description = "Quantidade de repetições que devem ser realizadas em cada série", example = "12", required = false)
    override var repetitions: Int? = null,

    @field:Schema(description = "Quantidade de séries que devem ser realizadas", example = "3", required = false)
    override var sets: Int? = null,

    @field:Schema(description = "Quantidade de descanso definida em milisegundos", example = "30000", required = false)
    override var rest: Long? = null,

    @field:Schema(description = "Ordem do exercício no treino", example = "1", required = false)
    override var exerciseOrder: Int? = null,

    @field:Schema(description = "Identificador do grupo muscular da predefinição. Opcional pois podem ser criados exercícios predefinidos avulsos.", required = false)
    override var workoutGroupPreDefinitionId: String? = null,

    @field:Schema(description = "Identificador do treinador pessoal da predefinição", required = true)
    @field:NotNull(message = "exercisePreDefinitionDTO.personalTrainerPersonId.notNull")
    override var personalTrainerPersonId: String? = null,
) : IExercisePreDefinitionDTO
