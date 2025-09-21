package br.com.fitnesspro.workout.repository.jpa.health

import br.com.fitnesspro.models.workout.health.HealthConnectSleepStages
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.WorkoutModuleImportationFilter

interface ICustomHealthConnectSleepStagesRepository {

    fun getHealthConnectSleepStagesImport(
        filter: WorkoutModuleImportationFilter,
        pageInfos: ImportPageInfos,
        sleepSessionIds: List<String>
    ): List<HealthConnectSleepStages>
}