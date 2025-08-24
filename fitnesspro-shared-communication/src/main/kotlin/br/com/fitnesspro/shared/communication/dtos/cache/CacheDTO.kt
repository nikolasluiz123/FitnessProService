package br.com.fitnesspro.shared.communication.dtos.cache

import br.com.fitnesspro.shared.communication.dtos.cache.interfaces.ICacheDTO

data class CacheDTO(
    override val cacheName: String = "",
): ICacheDTO