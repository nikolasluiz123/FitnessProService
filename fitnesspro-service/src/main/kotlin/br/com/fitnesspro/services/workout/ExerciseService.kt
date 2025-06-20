package br.com.fitnesspro.services.workout

import br.com.fitnesspro.config.application.cache.EXERCISE_IMPORT_CACHE_NAME
import br.com.fitnesspro.config.application.cache.VIDEO_EXERCISE_IMPORT_CACHE_NAME
import br.com.fitnesspro.repository.workout.ICustomExerciseRepository
import br.com.fitnesspro.repository.workout.ICustomVideoExerciseRepository
import br.com.fitnesspro.repository.workout.IExerciseRepository
import br.com.fitnesspro.services.mappers.ExerciseServiceMapper
import br.com.fitnesspro.shared.communication.dtos.workout.ExerciseDTO
import br.com.fitnesspro.shared.communication.dtos.workout.VideoExerciseDTO
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.WorkoutModuleImportFilter
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class ExerciseService(
    private val exerciseRepository: IExerciseRepository,
    private val customExerciseRepository: ICustomExerciseRepository,
    private val customVideoExerciseRepository: ICustomVideoExerciseRepository,
    private val workoutService: WorkoutService,
    private val exerciseServiceMapper: ExerciseServiceMapper
) {
    @CacheEvict(cacheNames = [EXERCISE_IMPORT_CACHE_NAME], allEntries = true)
    fun saveExercise(exerciseDTO: ExerciseDTO) {
        workoutService.saveWorkoutGroup(exerciseDTO.workoutGroupDTO!!)

        val exercise = exerciseServiceMapper.getExercise(exerciseDTO)
        exerciseRepository.save(exercise)
    }

    @Cacheable(cacheNames = [EXERCISE_IMPORT_CACHE_NAME], key = "#filter.toCacheKey()")
    fun getExercisesImport(filter: WorkoutModuleImportFilter, pageInfos: ImportPageInfos): List<ExerciseDTO> {
        return customExerciseRepository.getExercisesImport(filter, pageInfos).map(exerciseServiceMapper::getExerciseDTO)
    }

    @Cacheable(cacheNames = [VIDEO_EXERCISE_IMPORT_CACHE_NAME], key = "#filter.toCacheKey()")
    fun getVideoExercisesImport(filter: WorkoutModuleImportFilter, pageInfos: ImportPageInfos): List<VideoExerciseDTO> {
        return customVideoExerciseRepository.getVideoExercisesImport(filter, pageInfos).map(exerciseServiceMapper::getVideoExerciseDTO)
    }
}