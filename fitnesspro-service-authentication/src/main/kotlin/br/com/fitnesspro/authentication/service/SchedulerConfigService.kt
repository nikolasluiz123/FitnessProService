package br.com.fitnesspro.authentication.service

import br.com.fitnesspro.authentication.repository.auditable.ISchedulerConfigRepository
import br.com.fitnesspro.authentication.repository.jpa.ICustomSchedulerConfigRepository
import br.com.fitnesspro.authentication.service.mappers.SchedulerConfigServiceMapper
import br.com.fitnesspro.core.cache.SCHEDULER_CONFIG_IMPORT_CACHE_NAME
import br.com.fitnesspro.core.exceptions.BusinessException
import br.com.fitnesspro.models.scheduler.SchedulerConfig
import br.com.fitnesspro.service.communication.cache.ImportationEntity
import br.com.fitnesspro.service.communication.dtos.scheduler.ValidatedSchedulerConfigDTO
import br.com.fitnesspro.shared.communication.dtos.scheduler.interfaces.ISchedulerConfigDTO
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.CommonImportFilter
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.context.MessageSource
import org.springframework.stereotype.Service
import java.util.*

@Service
class SchedulerConfigService(
    private val schedulerConfigRepository: ISchedulerConfigRepository,
    private val customSchedulerConfigRepository: ICustomSchedulerConfigRepository,
    private val schedulerConfigServiceMapper: SchedulerConfigServiceMapper,
    private val messageSource: MessageSource
) {

    @Cacheable(cacheNames = [SCHEDULER_CONFIG_IMPORT_CACHE_NAME], keyGenerator = "importationKeyGenerator")
    @ImportationEntity(entitySimpleName = "SchedulerConfig")
    fun getSchedulerConfigsImport(
        filter: CommonImportFilter,
        pageInfos: ImportPageInfos
    ): List<ValidatedSchedulerConfigDTO> {
        return customSchedulerConfigRepository.getSchedulerConfigImport(filter, pageInfos)
            .map(schedulerConfigServiceMapper::getValidatedSchedulerConfigDTO)
    }

    @CacheEvict(cacheNames = [SCHEDULER_CONFIG_IMPORT_CACHE_NAME], allEntries = true)
    fun saveSchedulerConfig(schedulerConfigDTO: ValidatedSchedulerConfigDTO) {
        val config = schedulerConfigServiceMapper.getSchedulerConfig(schedulerConfigDTO)

        validateDensityRange(config)

        schedulerConfigRepository.save(config)
    }

    private fun validateDensityRange(config: SchedulerConfig) {
        if (config.minScheduleDensity > config.maxScheduleDensity ||
            config.minScheduleDensity == config.maxScheduleDensity) {
            throw BusinessException(
                messageSource.getMessage(
                    "scheduler.config.error.invalid.density.range",
                    null,
                    Locale.getDefault()
                )
            )
        }
    }

    @CacheEvict(cacheNames = [SCHEDULER_CONFIG_IMPORT_CACHE_NAME], allEntries = true)
    fun saveSchedulerConfigBatch(list: List<ISchedulerConfigDTO>) {
        val configs = list.map {
            val config = schedulerConfigServiceMapper.getSchedulerConfig(it)
            validateDensityRange(config)

            config
        }

        schedulerConfigRepository.saveAll(configs)
    }
}