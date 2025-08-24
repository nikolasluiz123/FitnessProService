package br.com.fitnesspro.shared.communication.dtos.cache

import br.com.fitnesspro.shared.communication.dtos.cache.interfaces.ICacheEntryDTO

data class CacheEntryDTO(
    override val key: String = ""
): ICacheEntryDTO