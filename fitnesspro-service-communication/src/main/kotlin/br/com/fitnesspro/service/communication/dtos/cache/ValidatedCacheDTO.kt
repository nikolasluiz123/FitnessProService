package br.com.fitnesspro.service.communication.dtos.cache

import br.com.fitnesspro.shared.communication.dtos.cache.interfaces.ICacheDTO
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

@Schema(description = "Classe DTO usada para a manutenção de um cache, agrupador de várias chaves")
data class ValidatedCacheDTO(
    @field:Schema(description = "Nome do cache", required = true, example = "v1.person.user.import")
    @field:NotBlank(message = "cacheDTO.cacheName.notBlank")
    override val cacheName: String = "",
): ICacheDTO