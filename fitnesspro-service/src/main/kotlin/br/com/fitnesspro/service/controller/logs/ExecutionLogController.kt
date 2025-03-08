package br.com.fitnesspro.service.controller.logs

import br.com.fitnesspro.service.config.gson.defaultGSon
import br.com.fitnesspro.service.service.logs.ExecutionsLogService
import br.com.fitnesspro.shared.communication.constants.EndPointsV1
import br.com.fitnesspro.shared.communication.constants.Timeouts
import br.com.fitnesspro.shared.communication.dtos.logs.ExecutionLogDTO
import br.com.fitnesspro.shared.communication.dtos.logs.UpdatableExecutionLogInfosDTO
import br.com.fitnesspro.shared.communication.filter.ExecutionLogsFilter
import br.com.fitnesspro.shared.communication.paging.CommonPageInfos
import br.com.fitnesspro.shared.communication.responses.PersistenceServiceResponse
import br.com.fitnesspro.shared.communication.responses.ReadServiceResponse
import br.com.fitnesspro.shared.communication.responses.SingleValueServiceResponse
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

    @PutMapping("/{id}")
    @Transactional(timeout = Timeouts.OPERATION_LOW_TIMEOUT)
    @SecurityRequirement(name = "Bearer Authentication")
    fun updateExecutionLog(@PathVariable id: String, @RequestBody clientInformation: UpdatableExecutionLogInfosDTO): ResponseEntity<PersistenceServiceResponse> {
        logService.updateLogWithClientInformation(id, clientInformation)
        return ResponseEntity.ok(PersistenceServiceResponse(code = HttpStatus.OK.value(), success = true))
    }

    @GetMapping
    @Transactional(timeout = Timeouts.OPERATION_MEDIUM_TIMEOUT)
    @SecurityRequirement(name = "Bearer Authentication")
    fun getListExecutionLog(@RequestParam filter: String, @RequestParam pageInfos: String): ResponseEntity<ReadServiceResponse<ExecutionLogDTO>> {
        val defaultGSon = GsonBuilder().defaultGSon()
        val queryFilter = defaultGSon.fromJson(filter, ExecutionLogsFilter::class.java)
        val commonPageInfos = defaultGSon.fromJson(filter, CommonPageInfos::class.java)

        val logs = logService.getListExecutionLog(queryFilter, commonPageInfos)
        return ResponseEntity.ok(ReadServiceResponse(values = logs, code = HttpStatus.OK.value(), success = true))
    }

    @GetMapping(EndPointsV1.LOGS_COUNT)
    @Transactional(timeout = Timeouts.OPERATION_MEDIUM_TIMEOUT)
    @SecurityRequirement(name = "Bearer Authentication")
    fun getCountListExecutionLog(@RequestParam filter: String, ): ResponseEntity<SingleValueServiceResponse<Int>> {
        val defaultGSon = GsonBuilder().defaultGSon()
        val queryFilter = defaultGSon.fromJson(filter, ExecutionLogsFilter::class.java)

        val count = logService.getCountListExecutionLog(queryFilter)
        return ResponseEntity.ok(SingleValueServiceResponse(value = count, code = HttpStatus.OK.value(), success = true))
    }
}