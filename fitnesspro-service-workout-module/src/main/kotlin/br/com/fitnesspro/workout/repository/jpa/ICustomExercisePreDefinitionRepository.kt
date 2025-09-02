package br.com.fitnesspro.workout.repository.jpa

import br.com.fitnesspro.models.workout.ExercisePreDefinition
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.WorkoutModuleImportationFilter

interface ICustomExercisePreDefinitionRepository {

    fun getExercisesPreDefinitionImport(filter: WorkoutModuleImportationFilter, pageInfos: ImportPageInfos): List<ExercisePreDefinition>

}