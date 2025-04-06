package br.com.fitnesspro.shared.communication.dtos.cache

data class CacheClearConfigDTO(
    var clearAll: Boolean = false,
    var cacheName: String? = null,
    var cacheKey: String? = null,
)
