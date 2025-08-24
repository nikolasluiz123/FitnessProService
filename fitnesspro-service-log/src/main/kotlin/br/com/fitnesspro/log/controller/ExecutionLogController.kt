package br.com.fitnesspro.log.controller

import br.com.fitnesspro.log.service.ExecutionsLogService
import br.com.fitnesspro.service.communication.dtos.logs.ValidatedExecutionLogDTO
import br.com.fitnesspro.service.communication.dtos.logs.ValidatedExecutionLogPackageDTO
import br.com.fitnesspro.service.communication.dtos.logs.ValidatedUpdatableExecutionLogInfosDTO
import br.com.fitnesspro.service.communication.dtos.logs.ValidatedUpdatableExecutionLogPackageInfosDTO
import br.com.fitnesspro.service.communication.gson.defaultServiceGSon
import br.com.fitnesspro.service.communication.responses.ValidatedPersistenceServiceResponse
import br.com.fitnesspro.service.communication.responses.ValidatedReadServiceResponse
import br.com.fitnesspro.service.communication.responses.ValidatedSingleValueServiceResponse
import br.com.fitnesspro.shared.communication.constants.EndPointsV1
import br.com.fitnesspro.shared.communication.constants.Timeouts
import br.com.fitnesspro.shared.communication.paging.CommonPageInfos
import br.com.fitnesspro.shared.communication.query.filter.ExecutionLogsFilter
import br.com.fitnesspro.shared.communication.query.filter.ExecutionLogsPackageFilter
import com.google.gson.GsonBuilder
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(EndPointsV1.LOGS_V1)
@Tag(name = "Execution Logs Controller", description = "Visualização dos Logs de Execução")
class ExecutionLogController(
    private val logService: ExecutionsLogService
) {

    @PutMapping("/{executionLogId}")
    @Transactional(timeout = Timeouts.OPERATION_LOW_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun updateExecutionLog(
        @PathVariable executionLogId: String,
        @RequestBody dto: ValidatedUpdatableExecutionLogInfosDTO
    ): ResponseEntity<ValidatedPersistenceServiceResponse<*>> {
        logService.updateExecutionLog(executionLogId, dto)
        return ResponseEntity.ok(
            ValidatedPersistenceServiceResponse(
                code = HttpStatus.OK.value(),
                success = true,
                savedDTO = null
            )
        )
    }

    @PutMapping("${EndPointsV1.LOGS_PACKAGE}/{executionLogPackageId}")
    @Transactional(timeout = Timeouts.OPERATION_LOW_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun updateExecutionLogPackage(
        @PathVariable executionLogPackageId: String,
        @RequestBody dto: ValidatedUpdatableExecutionLogPackageInfosDTO
    ): ResponseEntity<ValidatedPersistenceServiceResponse<*>> {
        logService.updateExecutionLogPackage(executionLogPackageId, dto)
        return ResponseEntity.ok(
            ValidatedPersistenceServiceResponse(
                code = HttpStatus.OK.value(),
                success = true,
                savedDTO = null
            )
        )
    }

    @GetMapping
    @Transactional(timeout = Timeouts.OPERATION_MEDIUM_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun getListExecutionLog(@RequestParam filter: String, @RequestParam pageInfos: String): ResponseEntity<ValidatedReadServiceResponse<ValidatedExecutionLogDTO>> {
        val defaultGSon = GsonBuilder().defaultServiceGSon()
        val queryFilter = defaultGSon.fromJson(filter, ExecutionLogsFilter::class.java)
        val commonPageInfos = defaultGSon.fromJson(pageInfos, CommonPageInfos::class.java)

        val logs = logService.getListExecutionLog(queryFilter, commonPageInfos)
        return ResponseEntity.ok(
            ValidatedReadServiceResponse(
                values = logs,
                code = HttpStatus.OK.value(),
                success = true
            )
        )
    }

    @GetMapping(EndPointsV1.LOGS_COUNT)
    @Transactional(timeout = Timeouts.OPERATION_MEDIUM_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun getCountListExecutionLog(@RequestParam filter: String, ): ResponseEntity<ValidatedSingleValueServiceResponse<Int>> {
        val defaultGSon = GsonBuilder().defaultServiceGSon()
        val queryFilter = defaultGSon.fromJson(filter, ExecutionLogsFilter::class.java)

        val count = logService.getCountListExecutionLog(queryFilter)
        return ResponseEntity.ok(
            ValidatedSingleValueServiceResponse(
                value = count,
                code = HttpStatus.OK.value(),
                success = true
            )
        )
    }

    @GetMapping(EndPointsV1.LOGS_PACKAGE)
    @Transactional(timeout = Timeouts.OPERATION_MEDIUM_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun getListExecutionLogPackage(@RequestParam filter: String, @RequestParam pageInfos: String): ResponseEntity<ValidatedReadServiceResponse<ValidatedExecutionLogPackageDTO>> {
        val defaultGSon = GsonBuilder().defaultServiceGSon()
        val queryFilter = defaultGSon.fromJson(filter, ExecutionLogsPackageFilter::class.java)
        val commonPageInfos = defaultGSon.fromJson(pageInfos, CommonPageInfos::class.java)

        val logs = logService.getListExecutionLogPackage(queryFilter, commonPageInfos)
        return ResponseEntity.ok(
            ValidatedReadServiceResponse(
                values = logs,
                code = HttpStatus.OK.value(),
                success = true
            )
        )
    }

    @GetMapping(EndPointsV1.LOGS_PACKAGE_COUNT)
    @Transactional(timeout = Timeouts.OPERATION_MEDIUM_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun getCountListExecutionLogPackage(@RequestParam filter: String): ResponseEntity<ValidatedSingleValueServiceResponse<Int>> {
        val defaultGSon = GsonBuilder().defaultServiceGSon()
        val queryFilter = defaultGSon.fromJson(filter, ExecutionLogsPackageFilter::class.java)

        val count = logService.getCountListExecutionLogPackage(queryFilter)
        return ResponseEntity.ok(
            ValidatedSingleValueServiceResponse(
                value = count,
                code = HttpStatus.OK.value(),
                success = true
            )
        )
    }
}