package br.com.fitnesspro.workout.repository.jpa

import br.com.fitnesspro.models.workout.ExerciseExecution
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.WorkoutModuleImportationFilter

interface ICustomExerciseExecutionRepository {

    fun getExercisesExecutionImport(filter: WorkoutModuleImportationFilter, pageInfos: ImportPageInfos): List<ExerciseExecution>

}