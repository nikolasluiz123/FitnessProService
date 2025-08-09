package br.com.fitnesspro.workout.repository.jpa

import br.com.fitnesspro.models.workout.VideoExercisePreDefinition
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.WorkoutModuleImportFilter

interface ICustomVideoExercisePreDefinitionRepository {

    fun getVideoExercisesPreDefinitionImport(filter: WorkoutModuleImportFilter, pageInfos: ImportPageInfos): List<VideoExercisePreDefinition>
}