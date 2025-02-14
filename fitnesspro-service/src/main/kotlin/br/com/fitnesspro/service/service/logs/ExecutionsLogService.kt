package br.com.fitnesspro.service.service.logs

import br.com.fitnesspro.models.executions.enums.EnumExecutionState
import br.com.fitnesspro.models.executions.enums.EnumExecutionType
import br.com.fitnesspro.models.executions.enums.EnumExecutionType.*
import br.com.fitnesspro.service.models.executions.ExecutionLog
import br.com.fitnesspro.service.repository.executions.IExecutionsLogRepository
import br.com.fitnesspro.shared.communication.dtos.logs.ExecutionLogDTO
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ExecutionsLogService(
    private val logRepository: IExecutionsLogRepository
) {

    fun saveLogPreHandle(request: HttpServletRequest) {
        val log = ExecutionLog(
            type = getExecutionType(request.method, request.requestURI),
            executionStart = LocalDateTime.now(),
            endPoint = request.requestURI,
            state = EnumExecutionState.RUNNING
        )

        request.setAttribute("logId", log.id)

        logRepository.save(log)
    }

    private fun getExecutionType(method: String?, requestURI: String): EnumExecutionType {
        return when {
            requestURI.contains("import") -> IMPORTATION
            requestURI.contains("export") -> EXPORTATION
            method == "GET" -> GET
            method == "POST" -> POST
            method == "PUT" -> PUT
            method == "DELETE" -> DELETE
            else -> {
                throw IllegalArgumentException("Tipo de execução não identificado")
            }
        }
    }

    fun updateLogAfterCompletion(request: HttpServletRequest, response: HttpServletResponse, ex: Exception?) {
        val logId = request.getAttribute("logId") as String
        val requestBody = request.getAttribute("logData") as? String

        val log = logRepository.findById(logId).orElseThrow()
        log.executionEnd = LocalDateTime.now()

        if (ex == null) {
            log.state = EnumExecutionState.FINISHED
            log.requestBody = requestBody
        } else {
            log.state = EnumExecutionState.ERROR
            log.requestBody = requestBody
            log.error = ex.stackTraceToString()
        }

        logRepository.save(log)
    }

    fun getAllLogs(): List<ExecutionLogDTO> {
        return logRepository.findAll().map { it.toExecutionLogDTO() }
    }

    private fun ExecutionLog.toExecutionLogDTO(): ExecutionLogDTO {
        return ExecutionLogDTO(
            id = id,
            type = type,
            state = state,
            executionStart = executionStart,
            executionEnd = executionEnd,
            endPoint = endPoint,
            requestBody = requestBody,
            error = error
        )
    }
}