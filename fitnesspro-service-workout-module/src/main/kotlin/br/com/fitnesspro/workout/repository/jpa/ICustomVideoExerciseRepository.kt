package br.com.fitnesspro.workout.repository.jpa

import br.com.fitnesspro.models.workout.VideoExercise
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.WorkoutModuleImportationFilter

interface ICustomVideoExerciseRepository {

    fun getVideoExercisesImport(filter: WorkoutModuleImportationFilter, pageInfos: ImportPageInfos): List<VideoExercise>
}