package br.com.fitnesspro.service.communication.dtos.workout

import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.IExerciseExecutionDTO
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.Instant
import java.time.LocalDateTime

@Schema(description = "Classe DTO usada para manutenção dos registros de execução dos exercícios do treino")
data class ValidatedExerciseExecutionDTO(
    @field:Schema(description = "Identificador", example = "e874d31c-0e29-4e9b-b48e-7d70d91b6a16", required = false)
    @field:Size(min = 1, max = 255, message = "baseDTO.id.size")
    override var id: String? = null,

    @field:Schema(description = "Data de criação", example = "2023-01-01T10:00:00", required = false, readOnly = true)
    override var creationDate: LocalDateTime? = null,

    @field:Schema(description = "Data de atualização", example = "2023-01-01T10:00:00", required = false, readOnly = true)
    override var updateDate: LocalDateTime? = null,

    @field:Schema(description = "Valor booleano que representa se o registro está ativo", required = true)
    @field:NotNull(message = "exerciseExecutionDTO.active.notNull")
    override var active: Boolean = true,

    @field:Schema(description = "Duração do exercício executado em milisegundos", example = "120000", required = false)
    @field:Min(value = 1, message = "exerciseExecutionDTO.duration.min")
    override var duration: Long? = null,

    @field:Schema(description = "Quantidade de repetições realizadas", example = "12", required = false)
    @field:Min(value = 1, message = "exerciseExecutionDTO.repetitions.min")
    override var repetitions: Int? = null,

    @field:Schema(description = "Série da execução atual", example = "3", required = false)
    @field:Min(value = 1, message = "exerciseExecutionDTO.set.min")
    override var set: Int? = null,

    @field:Schema(description = "Quantidade de descanso realizado em milisegundos", example = "30000", required = false)
    @field:Min(value = 1, message = "exerciseExecutionDTO.rest.min")
    override var rest: Long? = null,

    @field:Schema(description = "Peso utilizado na execução do exercício", example = "80.0", required = false)
    override var weight: Double? = null,

    @field:Schema(description = "Flag que indica se já houve a coleta de dados dos sensores para essa execução", required = true)
    @field:NotNull(message = "exerciseExecutionDTO.healthDataCollected.notNull")
    override var healthDataCollected: Boolean = false,

    @field:Schema(description = "Início da execução do exercício", required = true)
    @field:NotNull(message = "exerciseExecutionDTO.executionStartTime.notNull")
    override var executionStartTime: Instant = Instant.now(),

    @field:Schema(description = "Fim da execução do exercício", required = true)
    override var executionEndTime: Instant? = null,

    @field:Schema(description = "Identificador do exercício executado", required = true)
    @field:NotNull(message = "exerciseExecutionDTO.exerciseId.notNull")
    @field:Size(min = 1, max = 255, message = "baseDTO.id.size")
    override var exerciseId: String? = null,
) : IExerciseExecutionDTO