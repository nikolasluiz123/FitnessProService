package br.com.fitnesspro.services.workout

import br.com.fitnesspro.config.application.cache.*
import br.com.fitnesspro.repository.auditable.workout.IExerciseExecutionRepository
import br.com.fitnesspro.repository.auditable.workout.IExercisePreDefinitionRepository
import br.com.fitnesspro.repository.auditable.workout.IExerciseRepository
import br.com.fitnesspro.repository.jpa.workout.ICustomExerciseExecutionRepository
import br.com.fitnesspro.repository.jpa.workout.ICustomExercisePreDefinitionRepository
import br.com.fitnesspro.repository.jpa.workout.ICustomExerciseRepository
import br.com.fitnesspro.services.mappers.ExerciseServiceMapper
import br.com.fitnesspro.shared.communication.dtos.workout.ExerciseDTO
import br.com.fitnesspro.shared.communication.dtos.workout.ExerciseExecutionDTO
import br.com.fitnesspro.shared.communication.dtos.workout.ExercisePreDefinitionDTO
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
    private val exercisePreDefinitionRepository: IExercisePreDefinitionRepository,
    private val customExerciseRepository: ICustomExerciseRepository,
    private val customExerciseExecutionRepository: ICustomExerciseExecutionRepository,
    private val customExercisePreDefinitionRepository: ICustomExercisePreDefinitionRepository,
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

    @Cacheable(cacheNames = [EXERCISE_EXECUTION_IMPORT_CACHE_NAME], key = "#filter.toCacheKey()")
    fun getExercisesExecutionImport(filter: WorkoutModuleImportFilter, pageInfos: ImportPageInfos): List<ExerciseExecutionDTO> {
        return customExerciseExecutionRepository.getExercisesExecutionImport(filter, pageInfos).map(exerciseServiceMapper::getExerciseExecutionDTO)
    }

    @CacheEvict(cacheNames = [EXERCISE_PRE_DEFINITION_IMPORT_CACHE_NAME], allEntries = true)
    fun saveExercisePreDefinitionBatch(exerciseDTOs: List<ExercisePreDefinitionDTO>) {
        val exercises = exerciseDTOs.map {
            exerciseServiceMapper.getExercisePreDefinition(dto = it)
        }

        exercisePreDefinitionRepository.saveAll(exercises)
    }

    @Cacheable(cacheNames = [EXERCISE_PRE_DEFINITION_IMPORT_CACHE_NAME], key = "#filter.toCacheKey()")
    fun getExercisesPredefinitionImport(filter: WorkoutModuleImportFilter, pageInfos: ImportPageInfos): List<ExercisePreDefinitionDTO> {
        return customExercisePreDefinitionRepository.getExercisesPreDefinitionImport(filter, pageInfos).map(exerciseServiceMapper::getExercisePreDefinitionDTO)
    }
}