package br.com.fitnesspro.service.communication.dtos.workout.health

import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.health.ISleepSessionExerciseExecutionDTO
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

@Schema(description = "DTO para associação entre Sessão de Sono e Execução de Exercício")
data class ValidatedSleepSessionExerciseExecutionDTO(
    @field:Schema(description = "Identificador", required = false)
    @field:Size(min = 1, max = 255, message = "baseDTO.id.size")
    override var id: String? = null,

    @field:Schema(description = "Data de criação", required = false, readOnly = true)
    override var creationDate: LocalDateTime? = null,

    @field:Schema(description = "Data de atualização", required = false, readOnly = true)
    override var updateDate: LocalDateTime? = null,

    @field:Schema(description = "Registro ativo", required = true)
    @field:NotNull(message = "sleepSessionExerciseExecution.active.notNull")
    override var active: Boolean = true,

    @field:Schema(description = "ID da Sessão de Sono", required = true)
    @field:NotNull(message = "sleepSessionExerciseExecution.sleepSessionId.notNull")
    @field:Size(min = 1, max = 255, message = "baseDTO.id.size")
    override var healthConnectSleepSessionId: String? = null,

    @field:Schema(description = "ID da Execução do Exercício", required = true)
    @field:NotNull(message = "sleepSessionExerciseExecution.exerciseExecutionId.notNull")
    @field:Size(min = 1, max = 255, message = "baseDTO.id.size")
    override var exerciseExecutionId: String? = null
) : ISleepSessionExerciseExecutionDTO