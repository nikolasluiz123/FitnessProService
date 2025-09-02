package br.com.fitnesspro.workout.repository.jpa

import br.com.fitnesspro.models.workout.WorkoutGroup
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.WorkoutModuleImportationFilter

interface ICustomWorkoutGroupRepository {

    fun getWorkoutGroupsImport(filter: WorkoutModuleImportationFilter, pageInfos: ImportPageInfos): List<WorkoutGroup>

    fun getListWorkoutGroupIdFromWorkout(workoutId: String): List<String>
}