package br.com.fitnesspro.workout.service

import br.com.fitnesspro.core.cache.WORKOUT_GROUP_IMPORT_CACHE_NAME
import br.com.fitnesspro.core.cache.WORKOUT_GROUP_PRE_DEFINITION_IMPORT_CACHE_NAME
import br.com.fitnesspro.core.cache.WORKOUT_IMPORT_CACHE_NAME
import br.com.fitnesspro.service.communication.cache.ImportationEntity
import br.com.fitnesspro.service.communication.dtos.workout.ValidatedWorkoutDTO
import br.com.fitnesspro.service.communication.dtos.workout.ValidatedWorkoutGroupDTO
import br.com.fitnesspro.service.communication.dtos.workout.ValidatedWorkoutGroupPreDefinitionDTO
import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.IWorkoutDTO
import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.IWorkoutGroupDTO
import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.IWorkoutGroupPreDefinitionDTO
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.WorkoutModuleImportationFilter
import br.com.fitnesspro.workout.repository.auditable.IWorkoutGroupPreDefinitionRepository
import br.com.fitnesspro.workout.repository.auditable.IWorkoutGroupRepository
import br.com.fitnesspro.workout.repository.auditable.IWorkoutRepository
import br.com.fitnesspro.workout.repository.jpa.ICustomWorkoutGroupPreDefinitionRepository
import br.com.fitnesspro.workout.repository.jpa.ICustomWorkoutGroupRepository
import br.com.fitnesspro.workout.repository.jpa.ICustomWorkoutRepository
import br.com.fitnesspro.workout.service.mappers.WorkoutServiceMapper
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class WorkoutService(
    private val workoutRepository: IWorkoutRepository,
    private val workoutGroupRepository: IWorkoutGroupRepository,
    private val workoutGroupPreDefinitionRepository: IWorkoutGroupPreDefinitionRepository,
    private val customWorkoutRepository: ICustomWorkoutRepository,
    private val customWorkoutGroupRepository: ICustomWorkoutGroupRepository,
    private val customWorkoutGroupPreDefinitionRepository: ICustomWorkoutGroupPreDefinitionRepository,
    private val workoutServiceMapper: WorkoutServiceMapper
) {

    @CacheEvict(cacheNames = [WORKOUT_IMPORT_CACHE_NAME], allEntries = true)
    fun saveWorkoutBatch(workouts: List<IWorkoutDTO>) {
        workoutRepository.saveAll(workouts.map(workoutServiceMapper::getWorkout))
    }

    @CacheEvict(cacheNames = [WORKOUT_GROUP_IMPORT_CACHE_NAME], allEntries = true)
    fun saveWorkoutGroupBatch(workoutGroups: List<IWorkoutGroupDTO>) {
        workoutGroupRepository.saveAll(workoutGroups.map(workoutServiceMapper::getWorkoutGroup))
    }

    @Cacheable(cacheNames = [WORKOUT_IMPORT_CACHE_NAME], keyGenerator = "importationKeyGenerator")
    @ImportationEntity(entitySimpleName = "Workout")
    fun getWorkoutsImport(filter: WorkoutModuleImportationFilter, pageInfos: ImportPageInfos): List<ValidatedWorkoutDTO> {
        return customWorkoutRepository.getWorkoutsImport(filter, pageInfos).map(workoutServiceMapper::getWorkoutDTO)
    }

    @Cacheable(cacheNames = [WORKOUT_GROUP_IMPORT_CACHE_NAME], keyGenerator = "importationKeyGenerator")
    @ImportationEntity(entitySimpleName = "WorkoutGroup")
    fun getWorkoutGroupsImport(filter: WorkoutModuleImportationFilter, pageInfos: ImportPageInfos): List<ValidatedWorkoutGroupDTO> {
        return customWorkoutGroupRepository.getWorkoutGroupsImport(filter, pageInfos).map(workoutServiceMapper::getWorkoutGroupDTO)
    }

    @CacheEvict(cacheNames = [WORKOUT_GROUP_PRE_DEFINITION_IMPORT_CACHE_NAME], allEntries = true)
    fun saveWorkoutGroupPreDefinitionBatch(workoutGroups: List<IWorkoutGroupPreDefinitionDTO>) {
        workoutGroupPreDefinitionRepository.saveAll(workoutGroups.map(workoutServiceMapper::getWorkoutGroupPreDefinition))
    }

    @Cacheable(cacheNames = [WORKOUT_GROUP_PRE_DEFINITION_IMPORT_CACHE_NAME], keyGenerator = "importationKeyGenerator")
    @ImportationEntity(entitySimpleName = "WorkoutGroupPreDefinition")
    fun getWorkoutGroupsPreDefinitionImport(filter: WorkoutModuleImportationFilter, pageInfos: ImportPageInfos): List<ValidatedWorkoutGroupPreDefinitionDTO> {
        return customWorkoutGroupPreDefinitionRepository.getWorkoutGroupsPreDefinitionImport(filter, pageInfos).map(workoutServiceMapper::getWorkoutGroupPreDefinitionDTO)
    }
}