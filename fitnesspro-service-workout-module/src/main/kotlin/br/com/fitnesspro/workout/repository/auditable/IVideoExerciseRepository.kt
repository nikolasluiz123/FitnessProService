package br.com.fitnesspro.workout.repository.auditable

import br.com.fitnesspro.jpa.IAuditableFitnessProRepository
import br.com.fitnesspro.models.workout.VideoExercise


interface IVideoExerciseRepository: IAuditableFitnessProRepository<VideoExercise> {

    fun findByExerciseId(exerciseId: String): List<VideoExercise>

    fun findByExerciseIdIn(exerciseIdList: List<String>): List<VideoExercise>
}