package br.com.fitnesspro.workout.service.health

import br.com.fitnesspro.service.communication.dtos.sync.ValidatedHealthConnectModuleSyncDTO
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.WorkoutModuleImportationFilter
import org.springframework.stereotype.Service

@Service
class HealthConnectModuleSyncService(
    private val generalDataService: HealthConnectGeneralDataService,
    private val heartRateService: HealthConnectHeartRateService,
    private val sleepService: HealthConnectSleepService
) {

    fun getImportationData(filter: WorkoutModuleImportationFilter, pageInfos: ImportPageInfos): ValidatedHealthConnectModuleSyncDTO {
        return ValidatedHealthConnectModuleSyncDTO(
            metadata = generalDataService.getMetadataImport(filter, pageInfos),
            steps = generalDataService.getStepsImport(filter, pageInfos),
            caloriesBurned = generalDataService.getCaloriesImport(filter, pageInfos),
            heartRateSessions = heartRateService.getHeartRateImport(filter, pageInfos),
            heartRateSamples = heartRateService.getHeartRateSamplesImport(filter, pageInfos),
            sleepSessions = sleepService.getSleepSessionImport(filter, pageInfos),
            sleepStages = sleepService.getSleepStagesImport(filter, pageInfos),
            sleepSessionAssociations = sleepService.getSleepSessionAssociationImport(filter, pageInfos)
        )
    }

    fun saveExportedData(syncDTO: ValidatedHealthConnectModuleSyncDTO) {
        generalDataService.saveMetadataBatch(syncDTO.metadata)
        
        generalDataService.saveStepsBatch(syncDTO.steps)
        generalDataService.saveCaloriesBatch(syncDTO.caloriesBurned)
        
        heartRateService.saveHeartRateBatch(syncDTO.heartRateSessions)
        heartRateService.saveHeartRateSamplesBatch(syncDTO.heartRateSamples)

        sleepService.saveSleepSessionBatch(syncDTO.sleepSessions)
        sleepService.saveSleepStagesBatch(syncDTO.sleepStages)
        sleepService.saveSleepSessionAssociationBatch(syncDTO.sleepSessionAssociations)
    }
}