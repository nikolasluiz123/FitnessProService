package br.com.fitnesspro.shared.communication.dtos.cache.interfaces

interface ICacheClearConfigDTO {
    var clearAll: Boolean
    var cacheName: String?
    var cacheKey: String?
}