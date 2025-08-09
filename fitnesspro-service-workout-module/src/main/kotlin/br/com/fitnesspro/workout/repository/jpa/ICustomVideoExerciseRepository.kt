package br.com.fitnesspro.workout.repository.jpa

import br.com.fitnesspro.models.workout.VideoExercise
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.WorkoutModuleImportFilter

interface ICustomVideoExerciseRepository {

    fun getVideoExercisesImport(filter: WorkoutModuleImportFilter, pageInfos: ImportPageInfos): List<VideoExercise>
}