package br.com.fitnesspro.services.cache

import br.com.fitnesspro.exception.BusinessException
import br.com.fitnesspro.shared.communication.dtos.cache.CacheClearConfigDTO
import br.com.fitnesspro.shared.communication.dtos.cache.CacheDTO
import br.com.fitnesspro.shared.communication.dtos.cache.CacheEntryDTO
import com.github.benmanes.caffeine.cache.Cache
import org.springframework.cache.CacheManager
import org.springframework.cache.caffeine.CaffeineCache
import org.springframework.stereotype.Service

@Service
class CacheService(
    private val cacheManager: CacheManager
) {

    fun getListCaches(): List<CacheDTO> {
        return cacheManager.cacheNames.mapNotNull {
            CacheDTO(cacheName = it)
        }
    }

    fun getListCacheEntries(cacheName: String): List<CacheEntryDTO> {
        val cache = cacheManager.getCache(cacheName) as? CaffeineCache
        val nativeCache = cache?.nativeCache as? Cache<Any, Any>

        return nativeCache?.asMap()?.entries?.map { CacheEntryDTO(it.key.toString()) } ?: emptyList()
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

    fun clearCache(config: CacheClearConfigDTO) {
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
                throw BusinessException("Valores inválidos informados nas configurações de limpeza de cache.")
            }
        }
    }
}