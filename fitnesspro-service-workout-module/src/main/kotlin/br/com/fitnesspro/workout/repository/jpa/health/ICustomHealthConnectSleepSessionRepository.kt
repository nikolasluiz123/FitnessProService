package br.com.fitnesspro.workout.repository.jpa.health

import br.com.fitnesspro.models.workout.health.HealthConnectSleepSession
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.WorkoutModuleImportationFilter

interface ICustomHealthConnectSleepSessionRepository {

    fun getHealthConnectSleepSessionImport(
        filter: WorkoutModuleImportationFilter,
        pageInfos: ImportPageInfos
    ): List<HealthConnectSleepSession>
}