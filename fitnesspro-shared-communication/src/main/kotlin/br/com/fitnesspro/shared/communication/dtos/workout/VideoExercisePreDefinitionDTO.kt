package br.com.fitnesspro.shared.communication.dtos.workout

import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.IVideoExercisePreDefinitionDTO
import java.time.LocalDateTime

data class VideoExercisePreDefinitionDTO(
    override var id: String? = null,
    override var creationDate: LocalDateTime? = null,
    override var updateDate: LocalDateTime? = null,
    override var active: Boolean = true,
    override var exercisePreDefinitionId: String? = null,
    override var videoId: String? = null
) : IVideoExercisePreDefinitionDTO