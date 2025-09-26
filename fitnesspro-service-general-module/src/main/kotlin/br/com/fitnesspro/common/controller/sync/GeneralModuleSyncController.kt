package br.com.fitnesspro.common.controller.sync

import br.com.fitnesspro.common.service.sync.GeneralModuleSyncService
import br.com.fitnesspro.log.enums.EnumRequestAttributes
import br.com.fitnesspro.service.communication.dtos.sync.ValidatedGeneralModuleSyncDTO
import br.com.fitnesspro.service.communication.gson.defaultServiceGSon
import br.com.fitnesspro.service.communication.responses.ValidatedExportationServiceResponse
import br.com.fitnesspro.service.communication.responses.ValidatedImportationServiceResponse
import br.com.fitnesspro.shared.communication.constants.EndPointsV1
import br.com.fitnesspro.shared.communication.constants.Timeouts
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.CommonImportFilter
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
@RequestMapping(EndPointsV1.SYNC_V1)
@Tag(name = "General Module Sync Controller", description = "Controle de Importação e Exportação de Dados do Módulo Geral")
class GeneralModuleSyncController(
    private val generalModuleSyncService: GeneralModuleSyncService
) {

    @GetMapping(EndPointsV1.SYNC_IMPORT_GENERAL)
    @Transactional(timeout = Timeouts.OPERATION_HIGH_TIMEOUT, rollbackFor = [Exception::class], readOnly = true)
    @SecurityRequirement(name = "Bearer Authentication")
    fun import(@RequestParam filter: String, @RequestParam pageInfos: String, request: HttpServletRequest): ResponseEntity<ValidatedImportationServiceResponse<ValidatedGeneralModuleSyncDTO>> {
        val defaultGSon = GsonBuilder().defaultServiceGSon()
        val queryFilter = defaultGSon.fromJson(filter, CommonImportFilter::class.java)
        val commonPageInfos = defaultGSon.fromJson(pageInfos, ImportPageInfos::class.java)

        val value = generalModuleSyncService.getImportationData(queryFilter, commonPageInfos)
        val logId = request.getAttribute(EnumRequestAttributes.LOG_ID.name) as String
        val logPackageId = request.getAttribute(EnumRequestAttributes.LOG_PACKAGE_ID.name) as String

        return ResponseEntity.ok(
            ValidatedImportationServiceResponse(
                executionLogId = logId,
                executionLogPackageId = logPackageId,
                value = value,
                code = HttpStatus.OK.value(),
                success = true,
            )
        )
    }

    @PostMapping(EndPointsV1.SYNC_EXPORT_GENERAL)
    @Transactional(timeout = Timeouts.OPERATION_HIGH_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun export(@RequestBody @Valid syncDTO: ValidatedGeneralModuleSyncDTO, request: HttpServletRequest): ResponseEntity<ValidatedExportationServiceResponse> {
        generalModuleSyncService.saveExportedData(syncDTO)

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
}