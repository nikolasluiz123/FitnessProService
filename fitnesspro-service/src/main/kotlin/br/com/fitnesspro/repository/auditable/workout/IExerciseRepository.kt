package br.com.fitnesspro.repository.auditable.workout

import br.com.fitnesspro.models.workout.Exercise
import br.com.fitnesspro.repository.common.IAuditableFitnessProRepository


interface IExerciseRepository: IAuditableFitnessProRepository<Exercise> {

    fun findByWorkoutGroupId(workoutGroupId: String): List<Exercise>
}