package br.com.fitnesspro.shared.communication.dtos.workout

import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.IExercisePreDefinitionDTO
import java.time.LocalDateTime

data class ExercisePreDefinitionDTO(
    override var id: String? = null,
    override var creationDate: LocalDateTime? = null,
    override var updateDate: LocalDateTime? = null,
    override var active: Boolean = true,
    override var name: String? = null,
    override var duration: Long? = null,
    override var repetitions: Int? = null,
    override var sets: Int? = null,
    override var rest: Long? = null,
    override var exerciseOrder: Int? = null,
    override var workoutGroupPreDefinitionId: String? = null,
    override var personalTrainerPersonId: String? = null,
) : IExercisePreDefinitionDTO
