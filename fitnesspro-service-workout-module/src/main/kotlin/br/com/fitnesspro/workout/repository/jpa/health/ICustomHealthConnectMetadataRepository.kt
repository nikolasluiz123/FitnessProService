package br.com.fitnesspro.workout.repository.jpa.health

import br.com.fitnesspro.models.workout.health.HealthConnectMetadata
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.WorkoutModuleImportationFilter

interface ICustomHealthConnectMetadataRepository {

    fun getHealthConnectMetadataImport(filter: WorkoutModuleImportationFilter, pageInfos: ImportPageInfos): List<HealthConnectMetadata>
}