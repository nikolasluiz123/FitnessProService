package br.com.fitnesspro.workout.service

import br.com.fitnesspro.core.cache.*
import br.com.fitnesspro.service.communication.dtos.sync.ValidatedWorkoutModuleSyncDTO
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.WorkoutModuleImportationFilter
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class WorkoutModuleSyncService(
    private val workoutService: WorkoutService,
    private val exerciseService: ExerciseService,
    private val videoService: VideoService
) {

    @Cacheable(
        cacheNames = [
            WORKOUT_IMPORT_CACHE_NAME,
            WORKOUT_GROUP_IMPORT_CACHE_NAME,
            WORKOUT_GROUP_PRE_DEFINITION_IMPORT_CACHE_NAME,
            EXERCISE_IMPORT_CACHE_NAME,
            EXERCISE_EXECUTION_IMPORT_CACHE_NAME,
            EXERCISE_PRE_DEFINITION_IMPORT_CACHE_NAME,
            VIDEO_EXERCISE_IMPORT_CACHE_NAME,
            VIDEO_EXERCISE_EXECUTION_IMPORT_CACHE_NAME,
            VIDEO_EXERCISE_PRE_DEFINITION_IMPORT_CACHE_NAME,
            VIDEO_IMPORT_CACHE_NAME,
        ],
        key = "#filter.toCacheKey()"
    )
    fun getImportationData(filter: WorkoutModuleImportationFilter, pageInfos: ImportPageInfos): ValidatedWorkoutModuleSyncDTO {
        return ValidatedWorkoutModuleSyncDTO(
            workouts = workoutService.getWorkoutsImport(filter, pageInfos),
            workoutGroups = workoutService.getWorkoutGroupsImport(filter, pageInfos),
            exercises = exerciseService.getExercisesImport(filter, pageInfos),
            videos = videoService.getVideosImport(filter, pageInfos),
            videoExercises = videoService.getVideoExercisesImport(filter, pageInfos),

            exerciseExecutions = exerciseService.getExercisesExecutionImport(filter, pageInfos),
            videoExerciseExecutions = videoService.getVideoExercisesExecutionImport(filter, pageInfos),

            workoutGroupsPreDefinitions = workoutService.getWorkoutGroupsPreDefinitionImport(filter, pageInfos),
            exercisePredefinitions = exerciseService.getExercisesPredefinitionImport(filter, pageInfos),
            videoExercisePreDefinitions = videoService.getVideoExercisesPreDefinitionImport(filter, pageInfos)
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

        workoutService.saveWorkoutGroupPreDefinitionBatch(syncDTO.workoutGroupsPreDefinitions)
        exerciseService.saveExercisePreDefinitionBatch(syncDTO.exercisePredefinitions)
        videoService.saveExercisePreDefinitionVideosBatch(syncDTO.videoExercisePreDefinitions)
    }
}