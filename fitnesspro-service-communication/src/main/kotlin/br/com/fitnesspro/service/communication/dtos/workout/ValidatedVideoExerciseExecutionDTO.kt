package br.com.fitnesspro.service.communication.dtos.workout

import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.IVideoExerciseExecutionDTO
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

@Schema(description = "DTO para associação do vídeo com o a execução do exercício")
data class ValidatedVideoExerciseExecutionDTO(
    @field:Schema(description = "Identificador", example = "e874d31c-0e29-4e9b-b48e-7d70d91b6a16", required = false)
    @field:Size(min = 1, max = 255, message = "baseDTO.id.size")
    override var id: String? = null,

    @field:Schema(description = "Data de criação", example = "2023-01-01T10:00:00", required = false, readOnly = true)
    override var creationDate: LocalDateTime? = null,

    @field:Schema(description = "Data de atualização", example = "2023-01-01T10:00:00", required = false, readOnly = true)
    override var updateDate: LocalDateTime? = null,

    @field:Schema(description = "Valor booleano que representa se o registro está ativo", required = true)
    @field:NotNull(message = "videoExerciseDTO.active.notNull")
    override var active: Boolean = true,

    @field:Schema(description = "Identificador do exercício", required = true)
    @field:NotNull(message = "videoExerciseDTO.exerciseId.notNull")
    override var exerciseExecutionId: String? = null,

    @field:Schema(description = "Identificador do vídeo", required = true)
    @field:NotNull(message = "videoExerciseExecutionDTO.videoId.notNull")
    override var videoId: String? = null
): IVideoExerciseExecutionDTO