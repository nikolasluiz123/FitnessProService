package br.com.fitnesspro.workout.repository.jpa

import br.com.fitnesspro.models.workout.Workout
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.WorkoutModuleImportationFilter

interface ICustomWorkoutRepository {

    fun getWorkoutsImport(filter: WorkoutModuleImportationFilter, pageInfos: ImportPageInfos): List<Workout>

}