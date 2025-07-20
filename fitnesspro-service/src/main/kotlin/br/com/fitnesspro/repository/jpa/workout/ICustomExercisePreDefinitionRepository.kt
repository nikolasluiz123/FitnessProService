package br.com.fitnesspro.repository.jpa.workout

import br.com.fitnesspro.models.workout.ExercisePreDefinition
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.WorkoutModuleImportFilter

interface ICustomExercisePreDefinitionRepository {

    fun getExercisesPreDefinitionImport(filter: WorkoutModuleImportFilter, pageInfos: ImportPageInfos): List<ExercisePreDefinition>

}