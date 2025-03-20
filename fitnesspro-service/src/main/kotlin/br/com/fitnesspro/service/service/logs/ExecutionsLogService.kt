package br.com.fitnesspro.service.service.logs

import br.com.fitnesspro.models.executions.enums.EnumExecutionType
import br.com.fitnesspro.models.executions.enums.EnumExecutionType.*
import br.com.fitnesspro.service.config.application.JWTService
import br.com.fitnesspro.service.models.general.User
import br.com.fitnesspro.service.models.logs.ExecutionLog
import br.com.fitnesspro.service.models.logs.ExecutionLogPackage
import br.com.fitnesspro.service.repository.executions.ICustomExecutionsLogRepository
import br.com.fitnesspro.service.repository.executions.IExecutionsLogPackageRepository
import br.com.fitnesspro.service.repository.executions.IExecutionsLogRepository
import br.com.fitnesspro.service.repository.general.user.IUserRepository
import br.com.fitnesspro.shared.communication.dtos.logs.ExecutionLogDTO
import br.com.fitnesspro.shared.communication.dtos.logs.ExecutionLogPackageDTO
import br.com.fitnesspro.shared.communication.dtos.logs.UpdatableExecutionLogInfosDTO
import br.com.fitnesspro.shared.communication.dtos.logs.UpdatableExecutionLogPackageInfosDTO
import br.com.fitnesspro.shared.communication.enums.execution.EnumExecutionState
import br.com.fitnesspro.shared.communication.paging.PageInfos
import br.com.fitnesspro.shared.communication.query.filter.ExecutionLogsFilter
import br.com.fitnesspro.shared.communication.query.filter.ExecutionLogsPackageFilter
import jakarta.persistence.EntityNotFoundException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Service
import org.springframework.web.method.HandlerMethod
import java.time.LocalDateTime

@Service
class ExecutionsLogService(
    private val logRepository: IExecutionsLogRepository,
    private val logPackageRepository: IExecutionsLogPackageRepository,
    private val customLogRepository: ICustomExecutionsLogRepository,
    private val jwtService: JWTService,
    private val userRepository: IUserRepository
) {

    fun saveLogPreHandle(request: HttpServletRequest, handler: HandlerMethod) {
        val authorizationHeader = request.getHeader("Authorization")

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            val token = authorizationHeader.substring(7)
            val user = userRepository.findByEmail(jwtService.extractEmail(token)!!)
            val executionType = getExecutionType(request.method, request.requestURI)

            if (executionType in listOf(IMPORTATION, EXPORTATION)) {
                // TODO - Client precisa enviar que terminou de executar. O servico nao tem como saber isso.

                val notFinishedExecutionLog = customLogRepository.findNotFinishedExecutionLog(
                    userEmail = user!!.email!!,
                    executionType = executionType,
                    endPoint = request.requestURI,
                    methodName = handler.method.name
                )

                notFinishedExecutionLog?.let { createExecutionPackageLog(it, request) } ?: createExecutionLog(request, handler, user)

            } else {
                createExecutionLog(request, handler, user)
            }

        } else {
            createExecutionLog(request, handler)
        }
    }

    private fun createExecutionPackageLog(notFinishedExecutionLog: ExecutionLog, request: HttpServletRequest) {
        val logPackage = ExecutionLogPackage(
            executionLog = notFinishedExecutionLog,
            serviceExecutionStart = LocalDateTime.now()
        )

        request.setAttribute("logId", notFinishedExecutionLog.id)
        request.setAttribute("logPackageId", logPackage.id)

        logPackageRepository.save(logPackage)
    }

    private fun createExecutionLog(request: HttpServletRequest, handler: HandlerMethod, user: User? = null) {
        val log = ExecutionLog(
            type = getExecutionType(request.method, request.requestURI),
            endPoint = request.requestURI,
            state = EnumExecutionState.RUNNING,
            methodName = handler.method.name,
            user = user
        )

        val logPackage = ExecutionLogPackage(
            executionLog = log,
            serviceExecutionStart = LocalDateTime.now()
        )

        request.setAttribute("logId", log.id)
        request.setAttribute("logPackageId", logPackage.id)

        logRepository.save(log)
        logPackageRepository.save(logPackage)
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
        val logPackageId = request.getAttribute("logPackageId") as String
        val requestBody = request.getAttribute("logData") as String?

        val log = logRepository.findById(logId).orElseThrow()
        val logPackage = logPackageRepository.findById(logPackageId).orElseThrow()
        logPackage.serviceExecutionEnd = LocalDateTime.now()
        logPackage.requestBody = requestBody

        if (ex != null) {
            log.state = EnumExecutionState.ERROR
            logPackage.error = ex.stackTraceToString()
        }

        if (log.type !in listOf(IMPORTATION, EXPORTATION)) {
            log.state = EnumExecutionState.FINISHED
        }

        logRepository.save(log)
        logPackageRepository.save(logPackage)
    }

    fun getListExecutionLog(filter: ExecutionLogsFilter, pageInfos: PageInfos): List<ExecutionLogDTO> {
        return customLogRepository.getListExecutionLog(filter, pageInfos).map { it.toExecutionLogDTO() }
    }

    fun getCountListExecutionLog(filter: ExecutionLogsFilter): Int {
        return customLogRepository.getCountListExecutionLog(filter)
    }

    fun getListExecutionLogPackage(filter: ExecutionLogsPackageFilter, pageInfos: PageInfos): List<ExecutionLogPackageDTO> {
        return customLogRepository.getListExecutionLogPackage(filter, pageInfos).map { it.toExecutionLogPackageDTO() }
    }

    fun getCountListExecutionLogPackage(filter: ExecutionLogsPackageFilter): Int {
        return customLogRepository.getCountListExecutionLogPackage(filter)
    }

    private fun ExecutionLog.toExecutionLogDTO(): ExecutionLogDTO {
        return ExecutionLogDTO(
            id = id,
            type = type,
            state = state,
            endPoint = endPoint,
            methodName = methodName,
            userEmail = user?.email,
            pageSize = pageSize,
            lastUpdateDate = lastUpdateDate,
            creationDate = creationDate
        )
    }

    private fun ExecutionLogPackage.toExecutionLogPackageDTO(): ExecutionLogPackageDTO {
        return ExecutionLogPackageDTO(
            id = id,
            executionLogId = executionLog.id,
            clientExecutionStart = clientExecutionStart,
            clientExecutionEnd = clientExecutionEnd,
            serviceExecutionStart = serviceExecutionStart,
            serviceExecutionEnd = serviceExecutionEnd,
            requestBody = requestBody,
            error = error,
            insertedItemsCount = insertedItemsCount,
            updatedItemsCount = updatedItemsCount,
            allItemsCount = allItemsCount
        )
    }

    fun updateExecutionLog(id: String, dto: UpdatableExecutionLogInfosDTO) {
        val log = logRepository.findById(id).orElseThrow {
            throw EntityNotFoundException("Não foi encontrado um ExecutionLog com o identificador $id")
        }

        log.apply {
            dto.pageSize?.let { pageSize = it }
            dto.lastUpdateDate?.let { lastUpdateDate = it }
        }

        logRepository.save(log)
    }

    fun updateExecutionLogPackage(id: String, dto: UpdatableExecutionLogPackageInfosDTO) {
        val logPackage = logPackageRepository.findById(id).orElseThrow {
            throw EntityNotFoundException("Não foi encontrado um ExecutionLogPackage com o identificador $id")
        }

        logPackage.apply {
            dto.insertedItemsCount?.let { insertedItemsCount = it }
            dto.updatedItemsCount?.let { updatedItemsCount = it }
            dto.allItemsCount?.let { allItemsCount = it }
            dto.clientExecutionStart?.let { clientExecutionStart = it }
            dto.clientExecutionEnd?.let { clientExecutionEnd = it }
        }

        logPackageRepository.save(logPackage)
    }
}