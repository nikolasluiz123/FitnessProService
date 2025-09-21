package br.com.fitnesspro.workout.repository.jpa.health

import br.com.fitnesspro.models.workout.health.HealthConnectHeartRate
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.WorkoutModuleImportationFilter

interface ICustomHealthConnectHeartRateRepository {

    fun getHealthConnectHeartRateImport(
        filter: WorkoutModuleImportationFilter,
        pageInfos: ImportPageInfos,
        exerciseExecutionIds: List<String>,
        metadataIds: List<String>
    ): List<HealthConnectHeartRate>
}