package br.com.fitnesspro.workout.repository.jpa

import br.com.fitnesspro.models.workout.VideoExerciseExecution
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.WorkoutModuleImportFilter

interface ICustomVideoExerciseExecutionRepository {

    fun getVideoExercisesExecutionImport(filter: WorkoutModuleImportFilter, pageInfos: ImportPageInfos): List<VideoExerciseExecution>
}