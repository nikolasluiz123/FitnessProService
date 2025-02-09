package br.com.fitnesspro.controller

import br.com.fitnesspro.controller.constants.EndPoints
import br.com.fitnesspro.controller.constants.Timeouts
import br.com.fitnesspro.controller.responses.PersistenceServiceResponse
import br.com.fitnesspro.dto.AcademyDTO
import br.com.fitnesspro.service.AcademyService
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(EndPoints.ACADEMY_V1)
@Tag(name = "Academy Controller", description = "Manutenção das Academias que serão disponibilizadas no App")
class AcademyController(
    private val academyService: AcademyService
) {

    @PostMapping
    @Transactional(timeout = Timeouts.LOW_TIMEOUT)
    @SecurityRequirement(name = "Bearer Authentication")
    fun saveAcademy(@RequestBody @Valid academyDTO: AcademyDTO): ResponseEntity<PersistenceServiceResponse> {
        academyService.saveAcademy(academyDTO)
        return ResponseEntity.ok(PersistenceServiceResponse(code = HttpStatus.OK.value(), success = true))
    }

    @PostMapping(EndPoints.ACADEMY_BATCH)
    @Transactional(timeout = Timeouts.HIGH_TIMEOUT)
    @SecurityRequirement(name = "Bearer Authentication")
    fun saveAcademyBatch(@RequestBody @Valid academyDTOList: List<AcademyDTO>): ResponseEntity<PersistenceServiceResponse> {
        academyService.saveAcademyBatch(academyDTOList)
        return ResponseEntity.ok(PersistenceServiceResponse(code = HttpStatus.OK.value(), success = true))
    }

}