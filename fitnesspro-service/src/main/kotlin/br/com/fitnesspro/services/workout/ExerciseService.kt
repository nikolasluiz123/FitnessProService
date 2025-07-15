package br.com.fitnesspro.services.workout

import br.com.fitnesspro.config.application.cache.EXERCISE_EXECUTION_IMPORT_CACHE_NAME
import br.com.fitnesspro.config.application.cache.EXERCISE_IMPORT_CACHE_NAME
import br.com.fitnesspro.config.application.cache.VIDEO_EXERCISE_EXECUTION_IMPORT_CACHE_NAME
import br.com.fitnesspro.config.application.cache.WORKOUT_GROUP_IMPORT_CACHE_NAME
import br.com.fitnesspro.repository.auditable.workout.IExerciseExecutionRepository
import br.com.fitnesspro.repository.auditable.workout.IExerciseRepository
import br.com.fitnesspro.repository.jpa.workout.ICustomExerciseExecutionRepository
import br.com.fitnesspro.repository.jpa.workout.ICustomExerciseRepository
import br.com.fitnesspro.services.mappers.ExerciseServiceMapper
import br.com.fitnesspro.shared.communication.dtos.workout.ExerciseDTO
import br.com.fitnesspro.shared.communication.dtos.workout.ExerciseExecutionDTO
import br.com.fitnesspro.shared.communication.dtos.workout.NewExerciseExecutionDTO
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.WorkoutModuleImportFilter
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class ExerciseService(
    private val exerciseRepository: IExerciseRepository,
    private val exerciseExecutionRepository: IExerciseExecutionRepository,
    private val customExerciseRepository: ICustomExerciseRepository,
    private val customExerciseExecutionRepository: ICustomExerciseExecutionRepository,
    private val workoutService: WorkoutService,
    private val videoService: VideoService,
    private val exerciseServiceMapper: ExerciseServiceMapper
) {
    @CacheEvict(cacheNames = [EXERCISE_IMPORT_CACHE_NAME, WORKOUT_GROUP_IMPORT_CACHE_NAME], allEntries = true)
    fun saveExercise(exerciseDTO: ExerciseDTO) {
        workoutService.saveWorkoutGroup(exerciseDTO.workoutGroupDTO!!)

        val exercise = exerciseServiceMapper.getExercise(exerciseDTO)
        exerciseRepository.save(exercise)
    }

    @Cacheable(cacheNames = [EXERCISE_IMPORT_CACHE_NAME], key = "#filter.toCacheKey()")
    fun getExercisesImport(filter: WorkoutModuleImportFilter, pageInfos: ImportPageInfos): List<ExerciseDTO> {
        return customExerciseRepository.getExercisesImport(filter, pageInfos).map(exerciseServiceMapper::getExerciseDTO)
    }

    /**
     * Função que salva os [br.com.fitnesspro.models.workout.Exercise] vindos de uma exportação do móvel.
     *
     * O [ExerciseDTO.workoutGroupDTO] é totalmente ignorado nesse processamento, pois, [WorkoutService.saveWorkoutGroupBatch]
     * já vai ter persistido o [br.com.fitnesspro.models.workout.WorkoutGroup] devido à ordem das execuções.
     */
    @CacheEvict(cacheNames = [EXERCISE_IMPORT_CACHE_NAME], allEntries = true)
    fun saveExerciseBatch(exerciseDTOs: List<ExerciseDTO>) {
        val exercises = exerciseDTOs.map {
            exerciseServiceMapper.getExercise(dto = it)
        }

        exerciseRepository.saveAll(exercises)
    }

    @CacheEvict(cacheNames = [EXERCISE_EXECUTION_IMPORT_CACHE_NAME, VIDEO_EXERCISE_EXECUTION_IMPORT_CACHE_NAME], allEntries = true)
    fun newExerciseExecution(newExerciseExecutionDTO: NewExerciseExecutionDTO) {
        val exerciseExecution = exerciseServiceMapper.getExerciseExecution(newExerciseExecutionDTO.exerciseExecutionDTO!!)
        exerciseExecutionRepository.save(exerciseExecution)

        videoService.createExerciseExecutionVideos(newExerciseExecutionDTO.videosDTO)
    }

    @CacheEvict(cacheNames = [EXERCISE_EXECUTION_IMPORT_CACHE_NAME], allEntries = true)
    fun saveExerciseExecution(exerciseExecutionDTO: ExerciseExecutionDTO) {
        val exerciseExecution = exerciseServiceMapper.getExerciseExecution(exerciseExecutionDTO)
        exerciseExecutionRepository.save(exerciseExecution)
    }

    @CacheEvict(cacheNames = [EXERCISE_EXECUTION_IMPORT_CACHE_NAME], allEntries = true)
    fun saveExerciseExecutionBatch(exerciseDTOs: List<ExerciseExecutionDTO>) {
        val exercises = exerciseDTOs.map(exerciseServiceMapper::getExerciseExecution)
        exerciseExecutionRepository.saveAll(exercises)
    }

    fun getExercisesExecutionImport(filter: WorkoutModuleImportFilter, pageInfos: ImportPageInfos): List<ExerciseExecutionDTO> {
        return customExerciseExecutionRepository.getExercisesExecutionImport(filter, pageInfos).map(exerciseServiceMapper::getExerciseExecutionDTO)
    }
}