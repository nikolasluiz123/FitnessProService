package br.com.fitnesspro.workout.repository.jpa.health

import br.com.fitnesspro.models.workout.health.HealthConnectSteps
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.WorkoutModuleImportationFilter

interface ICustomHealthConnectStepsRepository {

    fun getHealthConnectStepsImport(filter: WorkoutModuleImportationFilter, pageInfos: ImportPageInfos): List<HealthConnectSteps>
}