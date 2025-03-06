package br.com.fitnesspro.service.service.logs

import br.com.fitnesspro.models.executions.enums.EnumExecutionType
import br.com.fitnesspro.models.executions.enums.EnumExecutionType.*
import br.com.fitnesspro.service.models.executions.ExecutionLog
import br.com.fitnesspro.service.repository.executions.ICustomExecutionsLogRepository
import br.com.fitnesspro.service.repository.executions.IExecutionsLogRepository
import br.com.fitnesspro.shared.communication.dtos.logs.ExecutionLogDTO
import br.com.fitnesspro.shared.communication.dtos.logs.UpdatableExecutionLogInfosDTO
import br.com.fitnesspro.shared.communication.enums.execution.EnumExecutionState
import br.com.fitnesspro.shared.communication.filter.ExecutionLogsFilter
import br.com.fitnesspro.shared.communication.paging.PageInfos
import jakarta.persistence.EntityNotFoundException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Service
import org.springframework.web.method.HandlerMethod
import java.time.LocalDateTime

@Service
class ExecutionsLogService(
    private val logRepository: IExecutionsLogRepository,
    private val customLogRepository: ICustomExecutionsLogRepository
) {

    fun saveLogPreHandle(request: HttpServletRequest, handler: HandlerMethod) {
        val log = ExecutionLog(
            type = getExecutionType(request.method, request.requestURI),
            serviceExecutionStart = LocalDateTime.now(),
            endPoint = request.requestURI,
            state = EnumExecutionState.RUNNING,
            methodName = handler.method.name
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
        log.serviceExecutionEnd = LocalDateTime.now()

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

    fun getListExecutionLog(filter: ExecutionLogsFilter, pageInfos: PageInfos): List<ExecutionLogDTO> {
        return customLogRepository.getListExecutionLog(filter, pageInfos).map { it.toExecutionLogDTO() }
    }

    fun getCountListExecutionLog(filter: ExecutionLogsFilter): Int {
        return customLogRepository.getCountListExecutionLog(filter)
    }

    private fun ExecutionLog.toExecutionLogDTO(): ExecutionLogDTO {
        return ExecutionLogDTO(
            id = id,
            type = type,
            state = state,
            serviceExecutionStart = serviceExecutionStart,
            serviceExecutionEnd = serviceExecutionEnd,
            endPoint = endPoint,
            requestBody = requestBody,
            error = error,
            methodName = methodName
        )
    }

    fun updateLogWithClientInformation(id: String, clientInformation: UpdatableExecutionLogInfosDTO) {
        val log = logRepository.findById(id).orElseThrow {
            throw EntityNotFoundException("Não foi encontrado um ExecutionLog com o identificador $id")
        }

        clientInformation.clientExecutionStart?.let { log.clientExecutionStart = it }
        clientInformation.clientExecutionEnd?.let { log.clientExecutionEnd = it }

        logRepository.save(log)
    }
}