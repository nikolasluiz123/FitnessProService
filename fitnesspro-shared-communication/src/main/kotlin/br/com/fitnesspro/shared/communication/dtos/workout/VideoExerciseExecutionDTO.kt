package br.com.fitnesspro.shared.communication.dtos.workout

import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.IVideoExerciseExecutionDTO
import java.time.LocalDateTime

data class VideoExerciseExecutionDTO(
    override var id: String? = null,
    override var creationDate: LocalDateTime? = null,
    override var updateDate: LocalDateTime? = null,
    override var active: Boolean = true,
    override var exerciseExecutionId: String? = null,
    override var videoId: String? = null
): IVideoExerciseExecutionDTO