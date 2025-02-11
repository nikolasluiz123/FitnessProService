package br.com.fitnesspro.service.executions

import br.com.fitnesspro.models.executions.ExecutionLog
import br.com.fitnesspro.models.executions.enums.EnumExecutionState
import br.com.fitnesspro.models.executions.enums.EnumExecutionType
import br.com.fitnesspro.models.executions.enums.EnumExecutionType.*
import br.com.fitnesspro.repository.executions.IExecutionsLogRepository
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
        return when (method) {
            "GET" -> GET
            "POST" -> POST
            else -> {
                if (requestURI.contains("import")) IMPORTATION else EXPORTATION
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
}