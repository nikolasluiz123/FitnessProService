package br.com.fitnesspro.repository.jpa.workout

import br.com.fitnesspro.models.workout.ExerciseExecution
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.WorkoutModuleImportFilter

interface ICustomExerciseExecutionRepository {

    fun getExercisesExecutionImport(filter: WorkoutModuleImportFilter, pageInfos: ImportPageInfos): List<ExerciseExecution>

}