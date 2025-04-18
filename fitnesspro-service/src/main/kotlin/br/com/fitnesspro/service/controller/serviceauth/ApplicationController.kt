package br.com.fitnesspro.service.controller.serviceauth

import br.com.fitnesspro.service.service.serviceauth.ApplicationService
import br.com.fitnesspro.shared.communication.constants.EndPointsV1
import br.com.fitnesspro.shared.communication.constants.Timeouts
import br.com.fitnesspro.shared.communication.dtos.serviceauth.ApplicationDTO
import br.com.fitnesspro.shared.communication.responses.PersistenceServiceResponse
import br.com.fitnesspro.shared.communication.responses.ReadServiceResponse
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
    @Transactional(timeout = Timeouts.OPERATION_LOW_TIMEOUT)
    @SecurityRequirement(name = "Bearer Authentication")
    fun saveApplication(@RequestBody @Valid applicationDTO: ApplicationDTO): ResponseEntity<PersistenceServiceResponse<ApplicationDTO>> {
        applicationService.saveApplication(applicationDTO)
        return ResponseEntity.ok(
            PersistenceServiceResponse(
                code = HttpStatus.OK.value(),
                success = true,
                savedDTO = applicationDTO
            )
        )
    }

    @GetMapping
    @Transactional(timeout = Timeouts.OPERATION_MEDIUM_TIMEOUT)
    @SecurityRequirement(name = "Bearer Authentication")
    fun getListApplications(): ResponseEntity<ReadServiceResponse<ApplicationDTO>> {
        val result = applicationService.getListApplications()
        return ResponseEntity.ok(ReadServiceResponse(values = result, code = HttpStatus.OK.value(), success = true))
    }


}