package br.com.fitnesspro.workout.service.health

import br.com.fitnesspro.core.cache.HEALTH_CONNECT_HEART_RATE_IMPORT_CACHE_NAME
import br.com.fitnesspro.core.cache.HEALTH_CONNECT_HEART_RATE_SAMPLES_IMPORT_CACHE_NAME
import br.com.fitnesspro.service.communication.dtos.workout.health.ValidatedHealthConnectHeartRateDTO
import br.com.fitnesspro.service.communication.dtos.workout.health.ValidatedHealthConnectHeartRateSamplesDTO
import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.health.IHealthConnectHeartRateDTO
import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.health.IHealthConnectHeartRateSamplesDTO
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.WorkoutModuleImportationFilter
import br.com.fitnesspro.workout.repository.auditable.health.IHealthConnectHeartRateRepository
import br.com.fitnesspro.workout.repository.auditable.health.IHealthConnectHeartRateSamplesRepository
import br.com.fitnesspro.workout.repository.jpa.health.ICustomHealthConnectHeartRateRepository
import br.com.fitnesspro.workout.repository.jpa.health.ICustomHealthConnectHeartRateSamplesRepository
import br.com.fitnesspro.workout.service.mappers.HealthConnectServiceMapper
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class HealthConnectHeartRateService(
    private val heartRateRepository: IHealthConnectHeartRateRepository,
    private val samplesRepository: IHealthConnectHeartRateSamplesRepository,
    private val customHeartRateRepository: ICustomHealthConnectHeartRateRepository,
    private val customSamplesRepository: ICustomHealthConnectHeartRateSamplesRepository,
    private val mapper: HealthConnectServiceMapper
) {

    @Cacheable(cacheNames = [HEALTH_CONNECT_HEART_RATE_IMPORT_CACHE_NAME], keyGenerator = "importationKeyGenerator")
    fun getHeartRateImport(
        filter: WorkoutModuleImportationFilter,
        pageInfos: ImportPageInfos,
        exerciseExecutionIds: List<String>,
        metadataIds: List<String>
    ): List<ValidatedHealthConnectHeartRateDTO> {
        return customHeartRateRepository.getHealthConnectHeartRateImport(filter, pageInfos, exerciseExecutionIds, metadataIds).map(mapper::getHealthConnectHeartRateDTO)
    }

    @CacheEvict(cacheNames = [HEALTH_CONNECT_HEART_RATE_IMPORT_CACHE_NAME], allEntries = true)
    fun saveHeartRateBatch(dtos: List<IHealthConnectHeartRateDTO>) {
        heartRateRepository.saveAll(dtos.map(mapper::getHealthConnectHeartRate))
    }

    @Cacheable(cacheNames = [HEALTH_CONNECT_HEART_RATE_SAMPLES_IMPORT_CACHE_NAME], keyGenerator = "importationKeyGenerator")
    fun getHeartRateSamplesImport(
        filter: WorkoutModuleImportationFilter,
        pageInfos: ImportPageInfos,
        heartRateSessionIds: List<String>
    ): List<ValidatedHealthConnectHeartRateSamplesDTO> {
        return customSamplesRepository.getHealthConnectHeartRateSamplesImport(filter, pageInfos, heartRateSessionIds).map(mapper::getHealthConnectHeartRateSamplesDTO)
    }

    @CacheEvict(cacheNames = [HEALTH_CONNECT_HEART_RATE_SAMPLES_IMPORT_CACHE_NAME], allEntries = true)
    fun saveHeartRateSamplesBatch(dtos: List<IHealthConnectHeartRateSamplesDTO>) {
        samplesRepository.saveAll(dtos.map(mapper::getHealthConnectHeartRateSamples))
    }
}