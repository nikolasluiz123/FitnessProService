package br.com.fitnesspro.workout.repository.jpa

import br.com.fitnesspro.models.workout.VideoExercisePreDefinition
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.WorkoutModuleImportationFilter

interface ICustomVideoExercisePreDefinitionRepository {

    fun getVideoExercisesPreDefinitionImport(filter: WorkoutModuleImportationFilter, pageInfos: ImportPageInfos): List<VideoExercisePreDefinition>
}