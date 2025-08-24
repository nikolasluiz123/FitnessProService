package br.com.fitnesspro.shared.communication.dtos.cache

import br.com.fitnesspro.shared.communication.dtos.cache.interfaces.ICacheClearConfigDTO

data class CacheClearConfigDTO(
    override var clearAll: Boolean = false,
    override var cacheName: String? = null,
    override var cacheKey: String? = null,
): ICacheClearConfigDTO
