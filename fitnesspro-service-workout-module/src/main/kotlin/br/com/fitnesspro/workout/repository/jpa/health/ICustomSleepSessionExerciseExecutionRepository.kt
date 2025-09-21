package br.com.fitnesspro.workout.repository.jpa.health

import br.com.fitnesspro.models.workout.health.SleepSessionExerciseExecution
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.WorkoutModuleImportationFilter

interface ICustomSleepSessionExerciseExecutionRepository {

    fun getSleepSessionExerciseExecutionImport(
        filter: WorkoutModuleImportationFilter,
        pageInfos: ImportPageInfos,
        sleepSessionIds: List<String>,
        exerciseExecutionIds: List<String>
    ): List<SleepSessionExerciseExecution>
}