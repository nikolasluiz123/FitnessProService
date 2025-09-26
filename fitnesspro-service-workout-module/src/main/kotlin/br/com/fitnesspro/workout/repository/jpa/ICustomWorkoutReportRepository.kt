package br.com.fitnesspro.workout.repository.jpa

import br.com.fitnesspro.models.general.WorkoutReport
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.WorkoutModuleImportationFilter

interface ICustomWorkoutReportRepository {

    fun getWorkoutReportsImport(
        filter: WorkoutModuleImportationFilter,
        pageInfos: ImportPageInfos
    ): List<WorkoutReport>

}