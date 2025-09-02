package br.com.fitnesspro.workout.repository.jpa

import br.com.fitnesspro.models.workout.WorkoutGroupPreDefinition
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.WorkoutModuleImportationFilter

interface ICustomWorkoutGroupPreDefinitionRepository {

    fun getWorkoutGroupsPreDefinitionImport(filter: WorkoutModuleImportationFilter, pageInfos: ImportPageInfos): List<WorkoutGroupPreDefinition>
}