package br.com.fitnesspro.service.controller.general

import br.com.fitnesspro.service.config.gson.defaultGSon
import br.com.fitnesspro.service.config.request.EnumRequestAttributes
import br.com.fitnesspro.service.service.general.AcademyService
import br.com.fitnesspro.shared.communication.constants.EndPointsV1
import br.com.fitnesspro.shared.communication.constants.Timeouts
import br.com.fitnesspro.shared.communication.dtos.general.AcademyDTO
import br.com.fitnesspro.shared.communication.paging.CommonPageInfos
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.AcademyFilter
import br.com.fitnesspro.shared.communication.query.filter.CommonImportFilter
import br.com.fitnesspro.shared.communication.responses.*
import com.google.gson.GsonBuilder
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping(EndPointsV1.ACADEMY_V1)
@Tag(name = "Academy Controller", description = "Manutenção das Academias que serão disponibilizadas no App")
class AcademyController(
    private val academyService: AcademyService
) {

    @PostMapping
    @Transactional(timeout = Timeouts.OPERATION_LOW_TIMEOUT)
    @SecurityRequirement(name = "Bearer Authentication")
    fun saveAcademy(@RequestBody @Valid academyDTO: AcademyDTO): ResponseEntity<PersistenceServiceResponse<AcademyDTO>> {
        academyService.saveAcademy(academyDTO)
        return ResponseEntity.ok(PersistenceServiceResponse(code = HttpStatus.OK.value(), success = true, savedDTO = academyDTO))
    }

    @PostMapping(EndPointsV1.ACADEMY_EXPORT)
    @Transactional(timeout = Timeouts.OPERATION_HIGH_TIMEOUT)
    @SecurityRequirement(name = "Bearer Authentication")
    fun saveAcademyBatch(@RequestBody @Valid academyDTOList: List<AcademyDTO>, request: HttpServletRequest): ResponseEntity<ExportationServiceResponse> {
        academyService.saveAcademyBatch(academyDTOList)

        val logId = request.getAttribute(EnumRequestAttributes.LOG_ID.name) as String
        val logPackageId = request.getAttribute(EnumRequestAttributes.LOG_PACKAGE_ID.name) as String
        return ResponseEntity.ok(
            ExportationServiceResponse(
                executionLogId = logId,
                executionLogPackageId = logPackageId,
                code = HttpStatus.OK.value(),
                success = true
            )
        )
    }

    @GetMapping(EndPointsV1.ACADEMY_IMPORT)
    @Transactional(timeout = Timeouts.OPERATION_MEDIUM_TIMEOUT)
    @SecurityRequirement(name = "Bearer Authentication")
    fun importAcademies(@RequestParam filter: String, @RequestParam pageInfos: String, request: HttpServletRequest): ResponseEntity<ImportationServiceResponse<AcademyDTO>> {
        val defaultGSon = GsonBuilder().defaultGSon()
        val commonImportFilter = defaultGSon.fromJson(filter, CommonImportFilter::class.java)
        val importPageInfos = defaultGSon.fromJson(pageInfos, ImportPageInfos::class.java)

        val values = academyService.getAcademiesImport(commonImportFilter, importPageInfos)
        val logId = request.getAttribute(EnumRequestAttributes.LOG_ID.name) as String
        val logPackageId = request.getAttribute(EnumRequestAttributes.LOG_PACKAGE_ID.name) as String
        return ResponseEntity.ok(
            ImportationServiceResponse(
                executionLogId = logId,
                executionLogPackageId = logPackageId,
                values = values,
                code = HttpStatus.OK.value(),
                success = true,
            )
        )
    }

    @GetMapping(EndPointsV1.ACADEMY_LIST)
    @Transactional(timeout = Timeouts.OPERATION_MEDIUM_TIMEOUT)
    @SecurityRequirement(name = "Bearer Authentication")
    fun getListAcademy(@RequestParam filter: String, @RequestParam pageInfos: String): ResponseEntity<ReadServiceResponse<AcademyDTO>> {
        val defaultGSon = GsonBuilder().defaultGSon()
        val queryFilter = defaultGSon.fromJson(filter, AcademyFilter::class.java)
        val commonPageInfos = defaultGSon.fromJson(pageInfos, CommonPageInfos::class.java)

        val values = academyService.getListAcademy(queryFilter, commonPageInfos)
        return ResponseEntity.ok(ReadServiceResponse(values = values, code = HttpStatus.OK.value(), success = true))
    }

    @GetMapping(EndPointsV1.ACADEMY_COUNT)
    @Transactional(timeout = Timeouts.OPERATION_MEDIUM_TIMEOUT)
    @SecurityRequirement(name = "Bearer Authentication")
    fun getCountListExecutionLog(@RequestParam filter: String): ResponseEntity<SingleValueServiceResponse<Int>> {
        val defaultGSon = GsonBuilder().defaultGSon()
        val queryFilter = defaultGSon.fromJson(filter, AcademyFilter::class.java)

        val count = academyService.getCountListAcademy(queryFilter)
        return ResponseEntity.ok(SingleValueServiceResponse(value = count, code = HttpStatus.OK.value(), success = true))
    }

}