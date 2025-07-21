package br.com.fitnesspro.shared.communication.dtos.workout

import br.com.fitnesspro.shared.communication.dtos.common.AuditableDTO
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

@Schema(description = "DTO para criação de um vídeo da execução de um exercício")
data class NewVideoExerciseExecutionDTO(
    @Schema(description = "Identificador", example = "e874d31c-0e29-4e9b-b48e-7d70d91b6a16", required = false)
    @field:Size(min = 1, max = 255, message = "baseDTO.id.size")
    override var id: String? = null,

    @Schema(description = "Data de criação", example = "2023-01-01T10:00:00", required = false, readOnly = true)
    override var creationDate: LocalDateTime? = null,

    @Schema(description = "Data de atualização", example = "2023-01-01T10:00:00", required = false, readOnly = true)
    override var updateDate: LocalDateTime? = null,

    @Schema(description = "Valor booleano que representa se o registro está ativo", required = true)
    @field:NotNull(message = "videoExerciseExecutionDTO.active.notNull")
    var active: Boolean = true,

    @Schema(description = "Identificador da execução do exercício", required = true)
    @field:NotNull(message = "videoExerciseExecutionDTO.exerciseExecutionId.notNull")
    var exerciseExecutionId: String? = null,

    @Schema(description = "DTO para representação do vídeo", required = true)
    @field:NotNull(message = "newVideoExerciseExecutionDTO.video.notNull")
    var videoDTO: VideoDTO? = null
): AuditableDTO