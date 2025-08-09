package br.com.fitnesspro.workout.repository.jpa

import br.com.fitnesspro.models.workout.Exercise
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.WorkoutModuleImportFilter

interface ICustomExerciseRepository {

    fun getExercisesImport(filter: WorkoutModuleImportFilter, pageInfos: ImportPageInfos): List<Exercise>

}