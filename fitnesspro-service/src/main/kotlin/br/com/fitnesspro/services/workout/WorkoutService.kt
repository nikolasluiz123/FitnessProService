package br.com.fitnesspro.services.workout

import br.com.fitnesspro.config.application.cache.WORKOUT_GROUP_IMPORT_CACHE_NAME
import br.com.fitnesspro.config.application.cache.WORKOUT_IMPORT_CACHE_NAME
import br.com.fitnesspro.repository.workout.ICustomWorkoutGroupRepository
import br.com.fitnesspro.repository.workout.ICustomWorkoutRepository
import br.com.fitnesspro.repository.workout.IWorkoutGroupRepository
import br.com.fitnesspro.repository.workout.IWorkoutRepository
import br.com.fitnesspro.services.mappers.WorkoutServiceMapper
import br.com.fitnesspro.shared.communication.dtos.workout.WorkoutDTO
import br.com.fitnesspro.shared.communication.dtos.workout.WorkoutGroupDTO
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.WorkoutModuleImportFilter
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class WorkoutService(
    private val workoutRepository: IWorkoutRepository,
    private val workoutGroupRepository: IWorkoutGroupRepository,
    private val customWorkoutRepository: ICustomWorkoutRepository,
    private val customWorkoutGroupRepository: ICustomWorkoutGroupRepository,
    private val workoutServiceMapper: WorkoutServiceMapper
) {

    @CacheEvict(cacheNames = [WORKOUT_IMPORT_CACHE_NAME], allEntries = true)
    fun saveWorkout(workoutDTO: WorkoutDTO) {
        workoutRepository.save(workoutServiceMapper.getWorkout(workoutDTO))
    }

    @CacheEvict(cacheNames = [WORKOUT_IMPORT_CACHE_NAME], allEntries = true)
    fun saveWorkoutBatch(workouts: List<WorkoutDTO>) {
        workoutRepository.saveAll(workouts.map(workoutServiceMapper::getWorkout))
    }

    @CacheEvict(cacheNames = [WORKOUT_GROUP_IMPORT_CACHE_NAME], allEntries = true)
    fun saveWorkoutGroup(workoutGroupDTO: WorkoutGroupDTO) {
        workoutGroupRepository.save(workoutServiceMapper.getWorkoutGroup(workoutGroupDTO))
    }

    @CacheEvict(cacheNames = [WORKOUT_GROUP_IMPORT_CACHE_NAME], allEntries = true)
    fun saveWorkoutGroupBatch(workoutGroups: List<WorkoutGroupDTO>) {
        workoutGroupRepository.saveAll(workoutGroups.map(workoutServiceMapper::getWorkoutGroup))
    }

    @Cacheable(cacheNames = [WORKOUT_IMPORT_CACHE_NAME], key = "#filter.toCacheKey()")
    fun getWorkoutsImport(filter: WorkoutModuleImportFilter, pageInfos: ImportPageInfos): List<WorkoutDTO> {
        return customWorkoutRepository.getWorkoutsImport(filter, pageInfos).map(workoutServiceMapper::getWorkoutDTO)
    }

    @Cacheable(cacheNames = [WORKOUT_GROUP_IMPORT_CACHE_NAME], key = "#filter.toCacheKey()")
    fun getWorkoutGroupsImport(filter: WorkoutModuleImportFilter, pageInfos: ImportPageInfos): List<WorkoutGroupDTO> {
        return customWorkoutGroupRepository.getWorkoutGroupsImport(filter, pageInfos).map(workoutServiceMapper::getWorkoutGroupDTO)
    }
}