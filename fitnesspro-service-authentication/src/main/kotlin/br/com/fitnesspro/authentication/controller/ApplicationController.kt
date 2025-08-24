package br.com.fitnesspro.authentication.controller

import br.com.fitnesspro.authentication.service.ApplicationService
import br.com.fitnesspro.service.communication.dtos.serviceauth.ValidatedApplicationDTO
import br.com.fitnesspro.service.communication.responses.ValidatedPersistenceServiceResponse
import br.com.fitnesspro.service.communication.responses.ValidatedReadServiceResponse
import br.com.fitnesspro.shared.communication.constants.EndPointsV1
import br.com.fitnesspro.shared.communication.constants.Timeouts
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(EndPointsV1.APPLICATION_V1)
@Tag(name = "Application Controller", description = "Manutenção das aplicações que integram com o serviço")
class ApplicationController(
    private val applicationService: ApplicationService
) {

    @PostMapping
    @Transactional(timeout = Timeouts.OPERATION_LOW_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun saveApplication(@RequestBody @Valid applicationDTO: ValidatedApplicationDTO): ResponseEntity<ValidatedPersistenceServiceResponse<ValidatedApplicationDTO>> {
        applicationService.saveApplication(applicationDTO)
        return ResponseEntity.ok(
            ValidatedPersistenceServiceResponse(
                code = HttpStatus.OK.value(),
                success = true,
                savedDTO = applicationDTO
            )
        )
    }

    @GetMapping
    @Transactional(timeout = Timeouts.OPERATION_MEDIUM_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun getListApplications(): ResponseEntity<ValidatedReadServiceResponse<ValidatedApplicationDTO>> {
        val result = applicationService.getListApplications()
        return ResponseEntity.ok(
            ValidatedReadServiceResponse(
                values = result,
                code = HttpStatus.OK.value(),
                success = true
            )
        )
    }


}