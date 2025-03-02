package br.com.fitnesspro.service.controller.general

import br.com.fitnesspro.service.service.general.AcademyService
import br.com.fitnesspro.shared.communication.constants.EndPointsV1
import br.com.fitnesspro.shared.communication.constants.Timeouts
import br.com.fitnesspro.shared.communication.dtos.general.AcademyDTO
import br.com.fitnesspro.shared.communication.filter.CommonImportFilter
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.responses.PersistenceServiceResponse
import br.com.fitnesspro.shared.communication.responses.ReadServiceResponse
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
@RequestMapping(EndPointsV1.ACADEMY_V1)
@Tag(name = "Academy Controller", description = "Manutenção das Academias que serão disponibilizadas no App")
class AcademyController(
    private val academyService: AcademyService
) {

    @PostMapping
    @Transactional(timeout = Timeouts.OPERATION_LOW_TIMEOUT)
    @SecurityRequirement(name = "Bearer Authentication")
    fun saveAcademy(@RequestBody @Valid academyDTO: AcademyDTO): ResponseEntity<PersistenceServiceResponse> {
        academyService.saveAcademy(academyDTO)
        return ResponseEntity.ok(PersistenceServiceResponse(code = HttpStatus.OK.value(), success = true))
    }

    @PostMapping(EndPointsV1.ACADEMY_EXPORT)
    @Transactional(timeout = Timeouts.OPERATION_HIGH_TIMEOUT)
    @SecurityRequirement(name = "Bearer Authentication")
    fun saveAcademyBatch(@RequestBody @Valid academyDTOList: List<AcademyDTO>): ResponseEntity<PersistenceServiceResponse> {
        academyService.saveAcademyBatch(academyDTOList)
        return ResponseEntity.ok(PersistenceServiceResponse(code = HttpStatus.OK.value(), success = true))
    }

    @PostMapping(EndPointsV1.ACADEMY_IMPORT)
    @Transactional(timeout = Timeouts.OPERATION_MEDIUM_TIMEOUT)
    @SecurityRequirement(name = "Bearer Authentication")
    fun importAcademies(@RequestBody filter: CommonImportFilter, pageInfos: ImportPageInfos): ResponseEntity<ReadServiceResponse<AcademyDTO>> {
        val users = academyService.getAcademiesImport(filter, pageInfos)
        return ResponseEntity.ok(ReadServiceResponse(values = users, code = HttpStatus.OK.value(), success = true))
    }

}