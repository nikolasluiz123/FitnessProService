package br.com.fitnesspro.workout.repository.jpa

import br.com.fitnesspro.models.workout.WorkoutGroup
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.WorkoutModuleImportFilter

interface ICustomWorkoutGroupRepository {

    fun getWorkoutGroupsImport(filter: WorkoutModuleImportFilter, pageInfos: ImportPageInfos): List<WorkoutGroup>

    fun getListWorkoutGroupIdFromWorkout(workoutId: String): List<String>
}