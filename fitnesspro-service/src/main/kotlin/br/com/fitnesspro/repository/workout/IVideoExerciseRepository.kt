package br.com.fitnesspro.repository.workout

import br.com.fitnesspro.models.workout.VideoExercise
import br.com.fitnesspro.repository.common.IAuditableFitnessProRepository


interface IVideoExerciseRepository: IAuditableFitnessProRepository<VideoExercise> {

    fun findByExerciseId(exerciseId: String): List<VideoExercise>

    fun findByExerciseIdIn(exerciseIdList: List<String>): List<VideoExercise>
}