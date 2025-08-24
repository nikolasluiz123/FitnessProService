package br.com.fitnesspro.shared.communication.dtos.workout

import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.IExerciseDTO
import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.IWorkoutGroupDTO
import java.time.LocalDateTime

data class ExerciseDTO(
    override var id: String? = null,
    override var creationDate: LocalDateTime? = null,
    override var updateDate: LocalDateTime? = null,
    override var active: Boolean = true,
    override var name: String? = null,
    override var duration: Long? = null,
    override var repetitions: Int? = null,
    override var sets: Int? = null,
    override var rest: Long? = null,
    override var observation: String? = null,
    override var exerciseOrder: Int? = null,
    override var workoutGroupDTO: IWorkoutGroupDTO? = null
) : IExerciseDTO