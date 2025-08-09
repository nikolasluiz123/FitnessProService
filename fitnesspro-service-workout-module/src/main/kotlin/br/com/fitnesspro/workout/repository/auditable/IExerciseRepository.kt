package br.com.fitnesspro.workout.repository.auditable

import br.com.fitnesspro.jpa.IAuditableFitnessProRepository
import br.com.fitnesspro.models.workout.Exercise


interface IExerciseRepository: IAuditableFitnessProRepository<Exercise> {

    fun findByWorkoutGroupId(workoutGroupId: String): List<Exercise>
}