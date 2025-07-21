package br.com.fitnesspro.shared.communication.dtos.workout

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull

@Schema(description = "Classe DTO usada apenas na criação do registro da execução de um exercício")
data class NewExerciseExecutionDTO(
    @Schema(description = "DTO da execução do exercício", required = true)
    @field:NotNull(message = "newExerciseExecutionDTO.exerciseExecutionDTO.notNull")
    var exerciseExecutionDTO: ExerciseExecutionDTO? = null,

    @Schema(description = "Lista de DTOs dos vídeos da execução do exercício", required = false)
    @field:NotNull(message = "newExerciseExecutionDTO.videosDTO.notNull")
    var videosDTO: List<NewVideoExerciseExecutionDTO> = emptyList()
)
