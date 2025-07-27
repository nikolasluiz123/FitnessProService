package br.com.fitnesspro.shared.communication.dtos.cache

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull

@Schema(description = "Classe DTO usada para as operações de limpeza de cache")
data class CacheClearConfigDTO(

    @field:Schema(description = "Flag que indica se deve limpar todos os caches", required = true, example = "false")
    @field:NotNull(message = "cacheClearConfigDTO.clearAll.notNull")
    var clearAll: Boolean = false,

    @field:Schema(description = "Nome do cache específico que deseja limpar", example = "v1.person.user.import")
    var cacheName: String? = null,

    @field:Schema(description = "Chave do cache que deseja limpar", example = "2023-01-01T10:00:00")
    var cacheKey: String? = null,
)
