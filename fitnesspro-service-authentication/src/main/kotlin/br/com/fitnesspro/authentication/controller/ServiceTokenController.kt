package br.com.fitnesspro.authentication.controller

import br.com.fitnesspro.authentication.service.TokenService
import br.com.fitnesspro.service.communication.dtos.serviceauth.ValidatedServiceTokenDTO
import br.com.fitnesspro.service.communication.dtos.serviceauth.ValidatedServiceTokenGenerationDTO
import br.com.fitnesspro.service.communication.gson.defaultServiceGSon
import br.com.fitnesspro.service.communication.responses.ValidatedFitnessProServiceResponse
import br.com.fitnesspro.service.communication.responses.ValidatedReadServiceResponse
import br.com.fitnesspro.service.communication.responses.ValidatedSingleValueServiceResponse
import br.com.fitnesspro.shared.communication.constants.EndPointsV1
import br.com.fitnesspro.shared.communication.constants.Timeouts
import br.com.fitnesspro.shared.communication.paging.CommonPageInfos
import br.com.fitnesspro.shared.communication.query.filter.ServiceTokenFilter
import com.google.gson.GsonBuilder
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(EndPointsV1.TOKEN_V1)
@Tag(name = "Service Token Controller", description = "Manuteção dos Tokens do Serviço")
class ServiceTokenController(
    private val tokenService: TokenService,
) {

    @PostMapping
    @Transactional(timeout = Timeouts.OPERATION_LOW_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun generateToken(@Valid @RequestBody dto: ValidatedServiceTokenGenerationDTO): ResponseEntity<ValidatedSingleValueServiceResponse<ValidatedServiceTokenDTO>> {
        val token = tokenService.generateServiceToken(dto)
        return ResponseEntity.ok(
            ValidatedSingleValueServiceResponse(
                value = token,
                code = HttpStatus.OK.value(),
                success = true
            )
        )
    }

    @PutMapping(EndPointsV1.TOKEN_INVALIDATE)
    @Transactional(timeout = Timeouts.OPERATION_LOW_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun invalidateToken(@PathVariable("id") tokenId: String): ResponseEntity<ValidatedFitnessProServiceResponse> {
        tokenService.invalidateToken(tokenId)
        return ResponseEntity.ok(ValidatedFitnessProServiceResponse(code = HttpStatus.OK.value(), success = true))
    }

    @PostMapping(EndPointsV1.TOKEN_INVALIDATE_ALL)
    @Transactional(timeout = Timeouts.OPERATION_MEDIUM_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun invalidateAllTokens(): ResponseEntity<ValidatedFitnessProServiceResponse> {
        tokenService.invalidateAllTokens()
        return ResponseEntity.ok(ValidatedFitnessProServiceResponse(code = HttpStatus.OK.value(), success = true))
    }

    @GetMapping
    @Transactional(timeout = Timeouts.OPERATION_LOW_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun getListServiceTokens(@RequestParam filter: String, @RequestParam pageInfos: String): ResponseEntity<ValidatedReadServiceResponse<ValidatedServiceTokenDTO>> {
        val defaultGSon = GsonBuilder().defaultServiceGSon()
        val queryFilter = defaultGSon.fromJson(filter, ServiceTokenFilter::class.java)
        val commonPageInfos = defaultGSon.fromJson(pageInfos, CommonPageInfos::class.java)

        val tokens = tokenService.getListServiceToken(queryFilter, commonPageInfos)
        return ResponseEntity.ok(
            ValidatedReadServiceResponse(
                values = tokens,
                code = HttpStatus.OK.value(),
                success = true
            )
        )
    }

    @GetMapping(EndPointsV1.TOKENS_COUNT)
    @Transactional(timeout = Timeouts.OPERATION_MEDIUM_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun getCountListServiceTokens(@RequestParam filter: String): ResponseEntity<ValidatedSingleValueServiceResponse<Int>> {
        val defaultGSon = GsonBuilder().defaultServiceGSon()
        val queryFilter = defaultGSon.fromJson(filter, ServiceTokenFilter::class.java)

        val count = tokenService.getCountListServiceToken(queryFilter)
        return ResponseEntity.ok(
            ValidatedSingleValueServiceResponse(
                value = count,
                code = HttpStatus.OK.value(),
                success = true
            )
        )
    }

    @GetMapping(EndPointsV1.TOKEN_SECRET)
    @Transactional(timeout = Timeouts.OPERATION_LOW_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun getSecretKey(): ResponseEntity<ValidatedSingleValueServiceResponse<String>> {
        val secret = tokenService.generateSecretKey()
        return ResponseEntity.ok(
            ValidatedSingleValueServiceResponse(
                value = secret,
                code = HttpStatus.OK.value(),
                success = true
            )
        )
    }

    @GetMapping(EndPointsV1.TOKEN_BY_ID)
    @Transactional(timeout = Timeouts.OPERATION_LOW_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun getToken(@PathVariable("id") tokenId: String): ResponseEntity<ValidatedSingleValueServiceResponse<ValidatedServiceTokenDTO>> {
        val token = tokenService.findServiceTokenDTOById(tokenId)
        return ResponseEntity.ok(
            ValidatedSingleValueServiceResponse(
                value = token,
                code = HttpStatus.OK.value(),
                success = true
            )
        )
    }

}