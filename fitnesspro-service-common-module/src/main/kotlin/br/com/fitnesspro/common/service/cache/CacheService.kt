package br.com.fitnesspro.common.service.cache

import br.com.fitnesspro.core.exceptions.BusinessException
import br.com.fitnesspro.service.communication.dtos.cache.ValidatedCacheClearConfigDTO
import br.com.fitnesspro.service.communication.dtos.cache.ValidatedCacheDTO
import br.com.fitnesspro.service.communication.dtos.cache.ValidatedCacheEntryDTO
import com.github.benmanes.caffeine.cache.Cache
import org.springframework.cache.CacheManager
import org.springframework.cache.caffeine.CaffeineCache
import org.springframework.context.MessageSource
import org.springframework.stereotype.Service
import java.util.*

@Service
class CacheService(
    private val cacheManager: CacheManager,
    private val messageSource: MessageSource
) {

    fun getListCaches(): List<ValidatedCacheDTO> {
        return cacheManager.cacheNames.mapNotNull {
            ValidatedCacheDTO(cacheName = it)
        }
    }

    fun getListCacheEntries(cacheName: String): List<ValidatedCacheEntryDTO> {
        val cache = cacheManager.getCache(cacheName) as? CaffeineCache
        val nativeCache = cache?.nativeCache as? Cache<Any, Any>

        return nativeCache?.asMap()?.entries?.map { ValidatedCacheEntryDTO(it.key.toString()) } ?: emptyList()
    }

    fun clearCacheWithName(name: String) {
        cacheManager.getCache(name)?.clear()
    }

    fun clearCacheWithKey(name: String, key: String) {
        cacheManager.getCache(name)?.evict(key)
    }

    fun clearAllCaches() {
        cacheManager.cacheNames.forEach {
            cacheManager.getCache(it)?.clear()
        }
    }

    fun clearCache(config: ValidatedCacheClearConfigDTO) {
        when {
            config.clearAll -> {
                clearAllCaches()
            }

            config.cacheName != null && config.cacheKey != null -> {
                clearCacheWithKey(config.cacheName!!, config.cacheKey!!)
            }

            config.cacheName != null -> {
                clearCacheWithName(config.cacheName!!)
            }

            else -> {
                val message = messageSource.getMessage("cache.invalid.values", null, Locale.getDefault())
                throw BusinessException(message)
            }
        }
    }
}