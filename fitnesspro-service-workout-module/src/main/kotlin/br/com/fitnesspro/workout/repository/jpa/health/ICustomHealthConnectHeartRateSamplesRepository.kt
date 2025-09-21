package br.com.fitnesspro.workout.repository.jpa.health

import br.com.fitnesspro.models.workout.health.HealthConnectHeartRateSamples
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.WorkoutModuleImportationFilter

interface ICustomHealthConnectHeartRateSamplesRepository {

    fun getHealthConnectHeartRateSamplesImport(
        filter: WorkoutModuleImportationFilter,
        pageInfos: ImportPageInfos,
        heartRateSessionIds: List<String>
    ): List<HealthConnectHeartRateSamples>
}