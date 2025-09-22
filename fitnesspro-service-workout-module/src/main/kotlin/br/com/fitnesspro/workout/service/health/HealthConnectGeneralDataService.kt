package br.com.fitnesspro.workout.service.health

import br.com.fitnesspro.core.cache.HEALTH_CONNECT_CALORIES_BURNED_IMPORT_CACHE_NAME
import br.com.fitnesspro.core.cache.HEALTH_CONNECT_METADATA_IMPORT_CACHE_NAME
import br.com.fitnesspro.core.cache.HEALTH_CONNECT_STEPS_IMPORT_CACHE_NAME
import br.com.fitnesspro.service.communication.cache.ImportationEntity
import br.com.fitnesspro.service.communication.dtos.workout.health.ValidatedHealthConnectCaloriesBurnedDTO
import br.com.fitnesspro.service.communication.dtos.workout.health.ValidatedHealthConnectMetadataDTO
import br.com.fitnesspro.service.communication.dtos.workout.health.ValidatedHealthConnectStepsDTO
import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.health.IHealthConnectCaloriesBurnedDTO
import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.health.IHealthConnectMetadataDTO
import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.health.IHealthConnectStepsDTO
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.WorkoutModuleImportationFilter
import br.com.fitnesspro.workout.repository.auditable.health.IHealthConnectCaloriesBurnedRepository
import br.com.fitnesspro.workout.repository.auditable.health.IHealthConnectMetadataRepository
import br.com.fitnesspro.workout.repository.auditable.health.IHealthConnectStepsRepository
import br.com.fitnesspro.workout.repository.jpa.health.ICustomHealthConnectCaloriesBurnedRepository
import br.com.fitnesspro.workout.repository.jpa.health.ICustomHealthConnectMetadataRepository
import br.com.fitnesspro.workout.repository.jpa.health.ICustomHealthConnectStepsRepository
import br.com.fitnesspro.workout.service.mappers.HealthConnectServiceMapper
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class HealthConnectGeneralDataService(
    private val metadataRepository: IHealthConnectMetadataRepository,
    private val stepsRepository: IHealthConnectStepsRepository,
    private val caloriesBurnedRepository: IHealthConnectCaloriesBurnedRepository,
    private val customMetadataRepository: ICustomHealthConnectMetadataRepository,
    private val customStepsRepository: ICustomHealthConnectStepsRepository,
    private val customCaloriesBurnedRepository: ICustomHealthConnectCaloriesBurnedRepository,
    private val mapper: HealthConnectServiceMapper
) {

    @Cacheable(cacheNames = [HEALTH_CONNECT_METADATA_IMPORT_CACHE_NAME], keyGenerator = "importationKeyGenerator")
    @ImportationEntity(entitySimpleName = "HealthConnectMetadata")
    fun getMetadataImport(filter: WorkoutModuleImportationFilter, pageInfos: ImportPageInfos): List<ValidatedHealthConnectMetadataDTO> {
        return customMetadataRepository.getHealthConnectMetadataImport(filter, pageInfos).map(mapper::getHealthConnectMetadataDTO)
    }

    @CacheEvict(cacheNames = [HEALTH_CONNECT_METADATA_IMPORT_CACHE_NAME], allEntries = true)
    fun saveMetadataBatch(dtos: List<IHealthConnectMetadataDTO>) {
        metadataRepository.saveAll(dtos.map(mapper::getHealthConnectMetadata))
    }

    @Cacheable(cacheNames = [HEALTH_CONNECT_STEPS_IMPORT_CACHE_NAME], keyGenerator = "importationKeyGenerator")
    @ImportationEntity(entitySimpleName = "HealthConnectSteps")
    fun getStepsImport(
        filter: WorkoutModuleImportationFilter,
        pageInfos: ImportPageInfos,
        exerciseExecutionIds: List<String>,
        metadataIds: List<String>
    ): List<ValidatedHealthConnectStepsDTO> {
        return customStepsRepository.getHealthConnectStepsImport(filter, pageInfos, exerciseExecutionIds, metadataIds).map(mapper::getHealthConnectStepsDTO)    }

    @CacheEvict(cacheNames = [HEALTH_CONNECT_STEPS_IMPORT_CACHE_NAME], allEntries = true)
    fun saveStepsBatch(dtos: List<IHealthConnectStepsDTO>) {
        stepsRepository.saveAll(dtos.map(mapper::getHealthConnectSteps))
    }

    @Cacheable(cacheNames = [HEALTH_CONNECT_CALORIES_BURNED_IMPORT_CACHE_NAME], keyGenerator = "importationKeyGenerator")
    @ImportationEntity(entitySimpleName = "HealthConnectCaloriesBurned")
    fun getCaloriesImport(
        filter: WorkoutModuleImportationFilter,
        pageInfos: ImportPageInfos,
        exerciseExecutionIds: List<String>,
        metadataIds: List<String>
    ): List<ValidatedHealthConnectCaloriesBurnedDTO> {
        return customCaloriesBurnedRepository.getHealthConnectCaloriesBurnedImport(filter, pageInfos, exerciseExecutionIds, metadataIds).map(mapper::getHealthConnectCaloriesBurnedDTO)
    }

    @CacheEvict(cacheNames = [HEALTH_CONNECT_CALORIES_BURNED_IMPORT_CACHE_NAME], allEntries = true)
    fun saveCaloriesBatch(dtos: List<IHealthConnectCaloriesBurnedDTO>) {
        caloriesBurnedRepository.saveAll(dtos.map(mapper::getHealthConnectCaloriesBurned))
    }
}