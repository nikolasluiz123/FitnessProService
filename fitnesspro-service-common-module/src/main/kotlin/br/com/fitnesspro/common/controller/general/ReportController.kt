package br.com.fitnesspro.common.controller.general

import br.com.fitnesspro.common.service.general.ReportService
import br.com.fitnesspro.log.enums.EnumRequestAttributes
import br.com.fitnesspro.service.communication.dtos.general.ValidatedReportDTO
import br.com.fitnesspro.service.communication.gson.defaultServiceGSon
import br.com.fitnesspro.service.communication.responses.ValidatedExportationServiceResponse
import br.com.fitnesspro.service.communication.responses.ValidatedImportationServiceResponse
import br.com.fitnesspro.shared.communication.constants.EndPointsV1
import br.com.fitnesspro.shared.communication.constants.Timeouts
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.ReportImportFilter
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
@RequestMapping(EndPointsV1.REPORT_V1)
@Tag(name = "Report Controller", description = "Operações com Relatórios")
class ReportController(
    private val reportService: ReportService,
) {

    @PostMapping(EndPointsV1.REPORT_EXPORT)
    @Transactional(timeout = Timeouts.OPERATION_HIGH_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun saveReportBatch(@RequestBody @Valid validatedReportDTOList: List<ValidatedReportDTO>, request: HttpServletRequest): ResponseEntity<ValidatedExportationServiceResponse> {
        reportService.saveReportBatch(validatedReportDTOList)

        val logId = request.getAttribute(EnumRequestAttributes.LOG_ID.name) as String
        val logPackageId = request.getAttribute(EnumRequestAttributes.LOG_PACKAGE_ID.name) as String

        return ResponseEntity.ok(
            ValidatedExportationServiceResponse(
                executionLogId = logId,
                executionLogPackageId = logPackageId,
                code = HttpStatus.OK.value(),
                success = true
            )
        )
    }

    @GetMapping(EndPointsV1.REPORT_IMPORT)
    @Transactional(timeout = Timeouts.OPERATION_MEDIUM_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun importReports(@RequestParam filter: String, @RequestParam pageInfos: String, request: HttpServletRequest): ResponseEntity<ValidatedImportationServiceResponse<ValidatedReportDTO>> {
        val defaultGSon = GsonBuilder().defaultServiceGSon()
        val importFilter = defaultGSon.fromJson(filter, ReportImportFilter::class.java)
        val importPageInfos = defaultGSon.fromJson(pageInfos, ImportPageInfos::class.java)

        val values = reportService.getReportsImport(importFilter, importPageInfos)
        val logId = request.getAttribute(EnumRequestAttributes.LOG_ID.name) as String
        val logPackageId = request.getAttribute(EnumRequestAttributes.LOG_PACKAGE_ID.name) as String

        return ResponseEntity.ok(
            ValidatedImportationServiceResponse(
                executionLogId = logId,
                executionLogPackageId = logPackageId,
                values = values,
                code = HttpStatus.OK.value(),
                success = true,
            )
        )
    }
}