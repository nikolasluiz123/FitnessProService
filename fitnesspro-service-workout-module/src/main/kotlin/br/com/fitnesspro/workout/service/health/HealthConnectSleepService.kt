package br.com.fitnesspro.workout.service.health

import br.com.fitnesspro.core.cache.HEALTH_CONNECT_SLEEP_SESSION_IMPORT_CACHE_NAME
import br.com.fitnesspro.core.cache.HEALTH_CONNECT_SLEEP_STAGES_IMPORT_CACHE_NAME
import br.com.fitnesspro.core.cache.SLEEP_SESSION_EXERCISE_EXECUTION_IMPORT_CACHE_NAME
import br.com.fitnesspro.service.communication.cache.ImportationEntity
import br.com.fitnesspro.service.communication.dtos.workout.health.ValidatedHealthConnectSleepSessionDTO
import br.com.fitnesspro.service.communication.dtos.workout.health.ValidatedHealthConnectSleepStagesDTO
import br.com.fitnesspro.service.communication.dtos.workout.health.ValidatedSleepSessionExerciseExecutionDTO
import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.health.IHealthConnectSleepSessionDTO
import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.health.IHealthConnectSleepStagesDTO
import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.health.ISleepSessionExerciseExecutionDTO
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.WorkoutModuleImportationFilter
import br.com.fitnesspro.workout.repository.auditable.health.IHealthConnectSleepSessionRepository
import br.com.fitnesspro.workout.repository.auditable.health.IHealthConnectSleepStagesRepository
import br.com.fitnesspro.workout.repository.auditable.health.ISleepSessionExerciseExecutionRepository
import br.com.fitnesspro.workout.repository.jpa.health.ICustomHealthConnectSleepSessionRepository
import br.com.fitnesspro.workout.repository.jpa.health.ICustomHealthConnectSleepStagesRepository
import br.com.fitnesspro.workout.repository.jpa.health.ICustomSleepSessionExerciseExecutionRepository
import br.com.fitnesspro.workout.service.mappers.HealthConnectServiceMapper
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class HealthConnectSleepService(
    private val sleepSessionRepository: IHealthConnectSleepSessionRepository,
    private val sleepStagesRepository: IHealthConnectSleepStagesRepository,
    private val associationRepository: ISleepSessionExerciseExecutionRepository,
    private val customSleepSessionRepository: ICustomHealthConnectSleepSessionRepository,
    private val customSleepStagesRepository: ICustomHealthConnectSleepStagesRepository,
    private val customAssociationRepository: ICustomSleepSessionExerciseExecutionRepository,
    private val mapper: HealthConnectServiceMapper
) {

    @Cacheable(cacheNames = [HEALTH_CONNECT_SLEEP_SESSION_IMPORT_CACHE_NAME], keyGenerator = "importationKeyGenerator")
    @ImportationEntity(entitySimpleName = "HealthConnectSleepSession")
    fun getSleepSessionImport(
        filter: WorkoutModuleImportationFilter,
        pageInfos: ImportPageInfos
    ): List<ValidatedHealthConnectSleepSessionDTO> {
        return customSleepSessionRepository.getHealthConnectSleepSessionImport(filter, pageInfos).map(mapper::getHealthConnectSleepSessionDTO)
    }

    @CacheEvict(cacheNames = [HEALTH_CONNECT_SLEEP_SESSION_IMPORT_CACHE_NAME], allEntries = true)
    fun saveSleepSessionBatch(dtos: List<IHealthConnectSleepSessionDTO>) {
        sleepSessionRepository.saveAll(dtos.map(mapper::getHealthConnectSleepSession))
    }

    @Cacheable(cacheNames = [HEALTH_CONNECT_SLEEP_STAGES_IMPORT_CACHE_NAME], keyGenerator = "importationKeyGenerator")
    @ImportationEntity(entitySimpleName = "HealthConnectSleepStages")
    fun getSleepStagesImport(
        filter: WorkoutModuleImportationFilter,
        pageInfos: ImportPageInfos
    ): List<ValidatedHealthConnectSleepStagesDTO> {
        return customSleepStagesRepository.getHealthConnectSleepStagesImport(filter, pageInfos).map(mapper::getHealthConnectSleepStagesDTO)
    }

    @CacheEvict(cacheNames = [HEALTH_CONNECT_SLEEP_STAGES_IMPORT_CACHE_NAME], allEntries = true)
    fun saveSleepStagesBatch(dtos: List<IHealthConnectSleepStagesDTO>) {
        sleepStagesRepository.saveAll(dtos.map(mapper::getHealthConnectSleepStages))
    }

    @Cacheable(cacheNames = [SLEEP_SESSION_EXERCISE_EXECUTION_IMPORT_CACHE_NAME], keyGenerator = "importationKeyGenerator")
    @ImportationEntity(entitySimpleName = "SleepSessionExerciseExecution")
    fun getSleepSessionAssociationImport(
        filter: WorkoutModuleImportationFilter,
        pageInfos: ImportPageInfos
    ): List<ValidatedSleepSessionExerciseExecutionDTO> {
        return customAssociationRepository.getSleepSessionExerciseExecutionImport(filter, pageInfos).map(mapper::getSleepSessionExerciseExecutionDTO)
    }

    @CacheEvict(cacheNames = [SLEEP_SESSION_EXERCISE_EXECUTION_IMPORT_CACHE_NAME], allEntries = true)
    fun saveSleepSessionAssociationBatch(dtos: List<ISleepSessionExerciseExecutionDTO>) {
        associationRepository.saveAll(dtos.map(mapper::getSleepSessionExerciseExecution))
    }
}