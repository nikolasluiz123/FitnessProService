package br.com.fitnesspro.controller.logs

import br.com.fitnesspro.controller.common.constants.EndPointsV1
import br.com.fitnesspro.controller.common.constants.Timeouts
import br.com.fitnesspro.controller.common.responses.ReadServiceResponse
import br.com.fitnesspro.dto.logs.ExecutionLogDTO
import br.com.fitnesspro.service.logs.ExecutionsLogService
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(EndPointsV1.LOGS_V1)
@Tag(name = "Execution Logs Controller", description = "Visualização dos Logs de Execução")
class ExecutionLogController(
    private val logService: ExecutionsLogService
) {

    @GetMapping
    @Transactional(timeout = Timeouts.MEDIUM_TIMEOUT)
    @SecurityRequirement(name = "Bearer Authentication")
    fun getAllLogs(): ResponseEntity<ReadServiceResponse<ExecutionLogDTO>> {
        val logs = logService.getAllLogs()
        return ResponseEntity.ok(ReadServiceResponse(values = logs, code = HttpStatus.OK.value(), success = true))
    }
}