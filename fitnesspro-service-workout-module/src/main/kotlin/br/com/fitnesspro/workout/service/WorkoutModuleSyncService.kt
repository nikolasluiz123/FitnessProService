package br.com.fitnesspro.workout.service

import br.com.fitnesspro.service.communication.dtos.sync.ValidatedWorkoutModuleSyncDTO
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.WorkoutModuleImportationFilter
import br.com.fitnesspro.workout.service.health.HealthConnectGeneralDataService
import br.com.fitnesspro.workout.service.health.HealthConnectHeartRateService
import br.com.fitnesspro.workout.service.health.HealthConnectSleepService
import org.springframework.stereotype.Service

@Service
class WorkoutModuleSyncService(
    private val workoutService: WorkoutService,
    private val exerciseService: ExerciseService,
    private val videoService: VideoService,
    private val healthConnectGeneralDataService: HealthConnectGeneralDataService,
    private val healthConnectHeartRateService: HealthConnectHeartRateService,
    private val healthConnectSleepService: HealthConnectSleepService
) {

    fun getImportationData(filter: WorkoutModuleImportationFilter, pageInfos: ImportPageInfos): ValidatedWorkoutModuleSyncDTO {
        val exerciseExecutions = exerciseService.getExercisesExecutionImport(filter, pageInfos)
        val metadata = healthConnectGeneralDataService.getMetadataImport(filter, pageInfos)

        val exerciseExecutionIds = exerciseExecutions.mapNotNull { it.id }
        val metadataIds = metadata.mapNotNull { it.id }

        val heartRateSessions = healthConnectHeartRateService.getHeartRateImport(filter, pageInfos, exerciseExecutionIds, metadataIds)
        val sleepSessions = healthConnectSleepService.getSleepSessionImport(filter, pageInfos, metadataIds)

        val heartRateSessionIds = heartRateSessions.mapNotNull { it.id }
        val sleepSessionIds = sleepSessions.mapNotNull { it.id }

        val steps = healthConnectGeneralDataService.getStepsImport(filter, pageInfos, exerciseExecutionIds, metadataIds)
        val caloriesBurned = healthConnectGeneralDataService.getCaloriesImport(filter, pageInfos, exerciseExecutionIds, metadataIds)
        val heartRateSamples = healthConnectHeartRateService.getHeartRateSamplesImport(filter, pageInfos, heartRateSessionIds)
        val sleepStages = healthConnectSleepService.getSleepStagesImport(filter, pageInfos, sleepSessionIds)
        val sleepSessionAssociations = healthConnectSleepService.getSleepSessionAssociationImport(filter, pageInfos, sleepSessionIds, exerciseExecutionIds)

        return ValidatedWorkoutModuleSyncDTO(
            workouts = workoutService.getWorkoutsImport(filter, pageInfos),
            workoutGroups = workoutService.getWorkoutGroupsImport(filter, pageInfos),
            exercises = exerciseService.getExercisesImport(filter, pageInfos),
            videos = videoService.getVideosImport(filter, pageInfos),
            videoExercises = videoService.getVideoExercisesImport(filter, pageInfos),
            exerciseExecutions = exerciseExecutions,
            videoExerciseExecutions = videoService.getVideoExercisesExecutionImport(filter, pageInfos),
            metadata = metadata,
            heartRateSessions = heartRateSessions,
            sleepSessions = sleepSessions,
            steps = steps,
            caloriesBurned = caloriesBurned,
            heartRateSamples = heartRateSamples,
            sleepStages = sleepStages,
            sleepSessionAssociations = sleepSessionAssociations,
            workoutGroupsPreDefinitions = workoutService.getWorkoutGroupsPreDefinitionImport(filter, pageInfos),
            exercisePredefinitions = exerciseService.getExercisesPredefinitionImport(filter, pageInfos),
            videoExercisePreDefinitions = videoService.getVideoExercisesPreDefinitionImport(filter, pageInfos),
        )
    }

    fun saveExportedData(syncDTO: ValidatedWorkoutModuleSyncDTO) {
        workoutService.saveWorkoutBatch(syncDTO.workouts)
        workoutService.saveWorkoutGroupBatch(syncDTO.workoutGroups)
        exerciseService.saveExerciseBatch(syncDTO.exercises)
        videoService.saveVideoBatch(syncDTO.videos)
        videoService.saveExerciseVideoBatch(syncDTO.videoExercises)

        exerciseService.saveExerciseExecutionBatch(syncDTO.exerciseExecutions)
        videoService.saveExerciseExecutionVideoBatch(syncDTO.videoExerciseExecutions)

        healthConnectGeneralDataService.saveMetadataBatch(syncDTO.metadata)
        healthConnectHeartRateService.saveHeartRateBatch(syncDTO.heartRateSessions)
        healthConnectSleepService.saveSleepSessionBatch(syncDTO.sleepSessions)

        healthConnectGeneralDataService.saveStepsBatch(syncDTO.steps)
        healthConnectGeneralDataService.saveCaloriesBatch(syncDTO.caloriesBurned)
        healthConnectHeartRateService.saveHeartRateSamplesBatch(syncDTO.heartRateSamples)
        healthConnectSleepService.saveSleepStagesBatch(syncDTO.sleepStages)
        healthConnectSleepService.saveSleepSessionAssociationBatch(syncDTO.sleepSessionAssociations)

        workoutService.saveWorkoutGroupPreDefinitionBatch(syncDTO.workoutGroupsPreDefinitions)
        exerciseService.saveExercisePreDefinitionBatch(syncDTO.exercisePredefinitions)
        videoService.saveExercisePreDefinitionVideosBatch(syncDTO.videoExercisePreDefinitions)
    }
}