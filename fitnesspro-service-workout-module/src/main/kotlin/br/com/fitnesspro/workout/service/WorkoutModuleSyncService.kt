package br.com.fitnesspro.workout.service

import br.com.fitnesspro.common.service.general.ReportService
import br.com.fitnesspro.service.communication.dtos.sync.ValidatedWorkoutModuleSyncDTO
import br.com.fitnesspro.shared.communication.enums.report.EnumReportContext
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.ReportImportFilter
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
    private val healthConnectSleepService: HealthConnectSleepService,
    private val workoutReportService: WorkoutReportService,
    private val reportService: ReportService
) {

    fun getImportationData(filter: WorkoutModuleImportationFilter, pageInfos: ImportPageInfos): ValidatedWorkoutModuleSyncDTO {
        val reportFilter = ReportImportFilter(
            personId = filter.personId,
            lastUpdateDateMap = filter.lastUpdateDateMap,
            reportContext = EnumReportContext.WORKOUT_REGISTER_EVOLUTION
        )

        return ValidatedWorkoutModuleSyncDTO(
            workouts = workoutService.getWorkoutsImport(filter, pageInfos),
            workoutGroups = workoutService.getWorkoutGroupsImport(filter, pageInfos),
            exercises = exerciseService.getExercisesImport(filter, pageInfos),
            videos = videoService.getVideosImport(filter, pageInfos),
            videoExercises = videoService.getVideoExercisesImport(filter, pageInfos),
            exerciseExecutions = exerciseService.getExercisesExecutionImport(filter, pageInfos),
            videoExerciseExecutions = videoService.getVideoExercisesExecutionImport(filter, pageInfos),
            metadata = healthConnectGeneralDataService.getMetadataImport(filter, pageInfos),
            heartRateSessions = healthConnectHeartRateService.getHeartRateImport(filter, pageInfos),
            sleepSessions = healthConnectSleepService.getSleepSessionImport(filter, pageInfos),
            steps = healthConnectGeneralDataService.getStepsImport(filter, pageInfos),
            caloriesBurned = healthConnectGeneralDataService.getCaloriesImport(filter, pageInfos),
            heartRateSamples = healthConnectHeartRateService.getHeartRateSamplesImport(filter, pageInfos),
            sleepStages = healthConnectSleepService.getSleepStagesImport(filter, pageInfos),
            sleepSessionAssociations = healthConnectSleepService.getSleepSessionAssociationImport(filter, pageInfos),
            workoutGroupsPreDefinitions = workoutService.getWorkoutGroupsPreDefinitionImport(filter, pageInfos),
            exercisePredefinitions = exerciseService.getExercisesPredefinitionImport(filter, pageInfos),
            videoExercisePreDefinitions = videoService.getVideoExercisesPreDefinitionImport(filter, pageInfos),
            reports = reportService.getReportsImport(reportFilter, pageInfos),
            workoutReports = workoutReportService.getWorkoutReportsImport(filter, pageInfos)
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

        reportService.saveReportBatch(syncDTO.reports)
        workoutReportService.saveWorkoutReportBatch(syncDTO.workoutReports)
    }
}