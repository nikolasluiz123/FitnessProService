package br.com.fitnesspro.repository.workout

import br.com.fitnesspro.models.workout.Workout
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.WorkoutModuleImportFilter

interface ICustomWorkoutRepository {

    fun getWorkoutsImport(filter: WorkoutModuleImportFilter, pageInfos: ImportPageInfos): List<Workout>

}