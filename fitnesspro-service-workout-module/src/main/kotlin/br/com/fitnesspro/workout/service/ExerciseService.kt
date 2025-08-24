package br.com.fitnesspro.workout.service

import br.com.fitnesspro.core.cache.EXERCISE_EXECUTION_IMPORT_CACHE_NAME
import br.com.fitnesspro.core.cache.EXERCISE_IMPORT_CACHE_NAME
import br.com.fitnesspro.core.cache.EXERCISE_PRE_DEFINITION_IMPORT_CACHE_NAME
import br.com.fitnesspro.service.communication.dtos.workout.ValidatedExerciseDTO
import br.com.fitnesspro.service.communication.dtos.workout.ValidatedExerciseExecutionDTO
import br.com.fitnesspro.service.communication.dtos.workout.ValidatedExercisePreDefinitionDTO
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.WorkoutModuleImportFilter
import br.com.fitnesspro.workout.repository.auditable.IExerciseExecutionRepository
import br.com.fitnesspro.workout.repository.auditable.IExercisePreDefinitionRepository
import br.com.fitnesspro.workout.repository.auditable.IExerciseRepository
import br.com.fitnesspro.workout.repository.jpa.ICustomExerciseExecutionRepository
import br.com.fitnesspro.workout.repository.jpa.ICustomExercisePreDefinitionRepository
import br.com.fitnesspro.workout.repository.jpa.ICustomExerciseRepository
import br.com.fitnesspro.workout.service.mappers.ExerciseServiceMapper
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import kotlin.collections.map

@Service
class ExerciseService(
    private val exerciseRepository: IExerciseRepository,
    private val exerciseExecutionRepository: IExerciseExecutionRepository,
    private val exercisePreDefinitionRepository: IExercisePreDefinitionRepository,
    private val customExerciseRepository: ICustomExerciseRepository,
    private val customExerciseExecutionRepository: ICustomExerciseExecutionRepository,
    private val customExercisePreDefinitionRepository: ICustomExercisePreDefinitionRepository,
    private val exerciseServiceMapper: ExerciseServiceMapper
) {

    @Cacheable(cacheNames = [EXERCISE_IMPORT_CACHE_NAME], key = "#filter.toCacheKey()")
    fun getExercisesImport(filter: WorkoutModuleImportFilter, pageInfos: ImportPageInfos): List<ValidatedExerciseDTO> {
        return customExerciseRepository.getExercisesImport(filter, pageInfos).map(exerciseServiceMapper::getExerciseDTO)
    }

    @CacheEvict(cacheNames = [EXERCISE_IMPORT_CACHE_NAME], allEntries = true)
    fun saveExerciseBatch(exerciseDTOs: List<ValidatedExerciseDTO>) {
        val exercises = exerciseDTOs.map {
            exerciseServiceMapper.getExercise(dto = it)
        }

        exerciseRepository.saveAll(exercises)
    }

    @CacheEvict(cacheNames = [EXERCISE_EXECUTION_IMPORT_CACHE_NAME], allEntries = true)
    fun saveExerciseExecutionBatch(exerciseDTOs: List<ValidatedExerciseExecutionDTO>) {
        val exercises = exerciseDTOs.map(exerciseServiceMapper::getExerciseExecution)
        exerciseExecutionRepository.saveAll(exercises)
    }

    @Cacheable(cacheNames = [EXERCISE_EXECUTION_IMPORT_CACHE_NAME], key = "#filter.toCacheKey()")
    fun getExercisesExecutionImport(filter: WorkoutModuleImportFilter, pageInfos: ImportPageInfos): List<ValidatedExerciseExecutionDTO> {
        return customExerciseExecutionRepository.getExercisesExecutionImport(filter, pageInfos).map(exerciseServiceMapper::getExerciseExecutionDTO)
    }

    @CacheEvict(cacheNames = [EXERCISE_PRE_DEFINITION_IMPORT_CACHE_NAME], allEntries = true)
    fun saveExercisePreDefinitionBatch(exerciseDTOs: List<ValidatedExercisePreDefinitionDTO>) {
        val exercises = exerciseDTOs.map {
            exerciseServiceMapper.getExercisePreDefinition(dto = it)
        }

        exercisePreDefinitionRepository.saveAll(exercises)
    }

    @Cacheable(cacheNames = [EXERCISE_PRE_DEFINITION_IMPORT_CACHE_NAME], key = "#filter.toCacheKey()")
    fun getExercisesPredefinitionImport(filter: WorkoutModuleImportFilter, pageInfos: ImportPageInfos): List<ValidatedExercisePreDefinitionDTO> {
        return customExercisePreDefinitionRepository.getExercisesPreDefinitionImport(filter, pageInfos).map(exerciseServiceMapper::getExercisePreDefinitionDTO)
    }
}