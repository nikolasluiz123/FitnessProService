package br.com.fitnesspro.shared.communication.dtos.cache

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

@Schema(description = "Classe DTO usada para manutenção das chaves de um cache específico")
data class CacheEntryDTO(

    @field:Schema(
        description = "Chave do cache, normalmente é uma data que representa o momento da leitura do dado",
        required = true,
        example = "2023-01-01T10:00:00"
    )
    @field:NotBlank(message = "cacheEntryDTO.key.notBlank")
    val key: String = ""
)