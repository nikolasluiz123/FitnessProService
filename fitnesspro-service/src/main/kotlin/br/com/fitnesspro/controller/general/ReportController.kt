package br.com.fitnesspro.controller.general

import br.com.fitnesspro.config.gson.defaultGSon
import br.com.fitnesspro.config.request.EnumRequestAttributes
import br.com.fitnesspro.services.general.ReportService
import br.com.fitnesspro.shared.communication.constants.EndPointsV1
import br.com.fitnesspro.shared.communication.constants.Timeouts
import br.com.fitnesspro.shared.communication.dtos.general.ReportDTO
import br.com.fitnesspro.shared.communication.dtos.general.SchedulerReportDTO
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.ReportImportFilter
import br.com.fitnesspro.shared.communication.query.filter.importation.SchedulerReportImportFilter
import br.com.fitnesspro.shared.communication.responses.ExportationServiceResponse
import br.com.fitnesspro.shared.communication.responses.ImportationServiceResponse
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
    private val reportService: ReportService
) {

    @PostMapping(EndPointsV1.SCHEDULER_REPORT_EXPORT)
    @Transactional(timeout = Timeouts.OPERATION_HIGH_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun saveSchedulerReportBatch(@RequestBody @Valid schedulerReportDTOList: List<SchedulerReportDTO>, request: HttpServletRequest): ResponseEntity<ExportationServiceResponse> {
        reportService.saveSchedulerReportBatch(schedulerReportDTOList)

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

    @PostMapping(EndPointsV1.REPORT_EXPORT)
    @Transactional(timeout = Timeouts.OPERATION_HIGH_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun saveReportBatch(@RequestBody @Valid reportDTOList: List<ReportDTO>, request: HttpServletRequest): ResponseEntity<ExportationServiceResponse> {
        reportService.saveReportBatch(reportDTOList)

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

    @GetMapping(EndPointsV1.SCHEDULER_REPORT_IMPORT)
    @Transactional(timeout = Timeouts.OPERATION_MEDIUM_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun importSchedulerReports(@RequestParam filter: String, @RequestParam pageInfos: String, request: HttpServletRequest): ResponseEntity<ImportationServiceResponse<SchedulerReportDTO>> {
        val defaultGSon = GsonBuilder().defaultGSon()
        val commonImportFilter = defaultGSon.fromJson(filter, SchedulerReportImportFilter::class.java)
        val importPageInfos = defaultGSon.fromJson(pageInfos, ImportPageInfos::class.java)

        val values = reportService.getSchedulerReportsImport(commonImportFilter, importPageInfos)
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

    @GetMapping(EndPointsV1.REPORT_IMPORT)
    @Transactional(timeout = Timeouts.OPERATION_MEDIUM_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun importReports(@RequestParam filter: String, @RequestParam pageInfos: String, request: HttpServletRequest): ResponseEntity<ImportationServiceResponse<ReportDTO>> {
        val defaultGSon = GsonBuilder().defaultGSon()
        val importFilter = defaultGSon.fromJson(filter, ReportImportFilter::class.java)
        val importPageInfos = defaultGSon.fromJson(pageInfos, ImportPageInfos::class.java)

        val values = reportService.getReportsImport(importFilter, importPageInfos)
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
}