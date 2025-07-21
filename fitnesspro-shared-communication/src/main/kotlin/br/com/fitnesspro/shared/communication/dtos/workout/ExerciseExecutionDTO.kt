package br.com.fitnesspro.shared.communication.dtos.workout

import br.com.fitnesspro.shared.communication.dtos.common.AuditableDTO
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

@Schema(description = "Classe DTO usada para manutenção dos registros de execução dos exercícios do treino")
data class ExerciseExecutionDTO(
    @Schema(description = "Identificador", example = "e874d31c-0e29-4e9b-b48e-7d70d91b6a16", required = false)
    @field:Size(min = 1, max = 255, message = "baseDTO.id.size")
    override var id: String? = null,

    @Schema(description = "Data de criação", example = "2023-01-01T10:00:00", required = false, readOnly = true)
    override var creationDate: LocalDateTime? = null,

    @Schema(description = "Data de atualização", example = "2023-01-01T10:00:00", required = false, readOnly = true)
    override var updateDate: LocalDateTime? = null,

    @Schema(description = "Valor booleano que representa se o registro está ativo", required = true)
    @field:NotNull(message = "exerciseExecutionDTO.active.notNull")
    var active: Boolean = true,

    @Schema(description = "Duração do exercício executado em milisegundos", example = "120000", required = false)
    @field:Min(value = 1, message = "exerciseExecutionDTO.duration.min")
    var duration: Long? = null,

    @Schema(description = "Quantidade de repetições realizadas", example = "12", required = false)
    @field:Min(value = 1, message = "exerciseExecutionDTO.repetitions.min")
    var repetitions: Int? = null,

    @Schema(description = "Série da execução atual", example = "3", required = false)
    @field:Min(value = 1, message = "exerciseExecutionDTO.set.min")
    var set: Int? = null,

    @Schema(description = "Quantidade de descanso realizado em milisegundos", example = "30000", required = false)
    @field:Min(value = 1, message = "exerciseExecutionDTO.rest.min")
    var rest: Long? = null,

    @Schema(description = "Peso utilizado na execução do exercício", example = "80.0", required = false)
    var weight: Double? = null,

    @Schema(description = "Data da execução do exercício", required = true, example = "2023-01-01T10:00:00")
    @field:NotNull(message = "exerciseExecutionDTO.date.notNull")
    var date: LocalDateTime? = null,

    @Schema(description = "Identificador do exercício executado", required = true)
    @field:NotNull(message = "exerciseExecutionDTO.exerciseId.notNull")
    @field:Size(min = 1, max = 255, message = "baseDTO.id.size")
    var exerciseId: String? = null,
) : AuditableDTO