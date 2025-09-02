package br.com.fitnesspro.common.controller.cache

import br.com.fitnesspro.common.service.cache.CacheService
import br.com.fitnesspro.service.communication.dtos.cache.ValidatedCacheClearConfigDTO
import br.com.fitnesspro.service.communication.dtos.cache.ValidatedCacheDTO
import br.com.fitnesspro.service.communication.dtos.cache.ValidatedCacheEntryDTO
import br.com.fitnesspro.service.communication.responses.ValidatedFitnessProServiceResponse
import br.com.fitnesspro.service.communication.responses.ValidatedReadServiceResponse
import br.com.fitnesspro.shared.communication.constants.EndPointsV1
import br.com.fitnesspro.shared.communication.constants.Timeouts
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping(EndPointsV1.CACHE_V1)
@Tag(name = "Cache Controller", description = "Visualização e Invalidação dos Caches do Serviço")
class CacheController(
    private val cacheService: CacheService
) {

    @GetMapping(EndPointsV1.CACHE_LIST)
    @Transactional(timeout = Timeouts.OPERATION_MEDIUM_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun getListCache(): ResponseEntity<ValidatedReadServiceResponse<ValidatedCacheDTO>> {
        val values = cacheService.getListCaches()
        return ResponseEntity.ok(
            ValidatedReadServiceResponse(
                values = values,
                code = HttpStatus.OK.value(),
                success = true
            )
        )
    }

    @GetMapping("${EndPointsV1.CACHE_ENTRIES}/{cacheName}")
    @Transactional(timeout = Timeouts.OPERATION_MEDIUM_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun getListCacheEntries(@PathVariable cacheName: String): ResponseEntity<ValidatedReadServiceResponse<ValidatedCacheEntryDTO>> {
        val values = cacheService.getListCacheEntries(cacheName)
        return ResponseEntity.ok(
            ValidatedReadServiceResponse(
                values = values,
                code = HttpStatus.OK.value(),
                success = true
            )
        )
    }

    @PostMapping(EndPointsV1.CACHE_CLEAR)
    @Transactional(timeout = Timeouts.OPERATION_LOW_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun clearCache(@RequestBody config: ValidatedCacheClearConfigDTO): ResponseEntity<ValidatedFitnessProServiceResponse> {
        cacheService.clearCache(config)
        return ResponseEntity.ok(ValidatedFitnessProServiceResponse(code = HttpStatus.OK.value(), success = true))
    }
}