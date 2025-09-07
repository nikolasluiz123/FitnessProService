package br.com.fitnesspro.log.service

import br.com.fitnesspro.authentication.repository.auditable.IDeviceRepository
import br.com.fitnesspro.authentication.repository.auditable.IUserRepository
import br.com.fitnesspro.authentication.repository.jpa.IApplicationRepository
import br.com.fitnesspro.authentication.service.TokenService
import br.com.fitnesspro.core.extensions.dateTimeNow
import br.com.fitnesspro.log.enums.EnumRequestAttributes
import br.com.fitnesspro.log.repository.jpa.ICustomExecutionsLogRepository
import br.com.fitnesspro.log.repository.jpa.IExecutionsLogPackageRepository
import br.com.fitnesspro.log.repository.jpa.IExecutionsLogRepository
import br.com.fitnesspro.log.service.mappers.LogsServiceMapper
import br.com.fitnesspro.models.logs.ExecutionLog
import br.com.fitnesspro.models.logs.ExecutionLogPackage
import br.com.fitnesspro.service.communication.dtos.logs.ValidatedExecutionLogDTO
import br.com.fitnesspro.service.communication.dtos.logs.ValidatedExecutionLogPackageDTO
import br.com.fitnesspro.service.communication.dtos.logs.ValidatedUpdatableExecutionLogInfosDTO
import br.com.fitnesspro.service.communication.dtos.logs.ValidatedUpdatableExecutionLogPackageInfosDTO
import br.com.fitnesspro.shared.communication.dtos.general.interfaces.IUserDTO
import br.com.fitnesspro.shared.communication.dtos.serviceauth.interfaces.IApplicationDTO
import br.com.fitnesspro.shared.communication.dtos.serviceauth.interfaces.IDeviceDTO
import br.com.fitnesspro.shared.communication.enums.execution.EnumExecutionState
import br.com.fitnesspro.shared.communication.enums.execution.EnumExecutionType
import br.com.fitnesspro.shared.communication.enums.execution.EnumExecutionType.*
import br.com.fitnesspro.shared.communication.paging.PageInfos
import br.com.fitnesspro.shared.communication.query.filter.ExecutionLogsFilter
import br.com.fitnesspro.shared.communication.query.filter.ExecutionLogsPackageFilter
import jakarta.persistence.EntityNotFoundException
import jakarta.servlet.http.HttpServletRequest
import org.springframework.context.MessageSource
import org.springframework.stereotype.Service
import org.springframework.web.method.HandlerMethod
import java.time.LocalDateTime
import java.util.*

@Service
class ExecutionsLogService(
    private val logRepository: IExecutionsLogRepository,
    private val logPackageRepository: IExecutionsLogPackageRepository,
    private val customLogRepository: ICustomExecutionsLogRepository,
    private val tokenService: TokenService,
    private val userRepository: IUserRepository,
    private val deviceRepository: IDeviceRepository,
    private val applicationRepository: IApplicationRepository,
    private val logsServiceMapper: LogsServiceMapper,
    private val messageSource: MessageSource
) {

    fun saveLogPreHandle(request: HttpServletRequest, handler: HandlerMethod) {
        val authorizationHeader = request.getHeader("Authorization")

        val token = authorizationHeader.substring(7)
        val serviceTokenDTO = tokenService.getServiceTokenDTO(token)
        val executionType = getExecutionType(request.method, request.requestURI)

        when {
            serviceTokenDTO?.user != null -> {
                if (executionType in listOf(IMPORTATION, EXPORTATION, STORAGE)) {
                    val notFinishedExecutionLog = customLogRepository.findNotFinishedExecutionLog(
                        userEmail = serviceTokenDTO.user?.email!!,
                        executionType = executionType,
                        endPoint = request.requestURI,
                        methodName = handler.method.name
                    )

                    if (notFinishedExecutionLog != null) {
                        createExecutionPackageLog(notFinishedExecutionLog, request)
                    } else {
                        createExecutionLog(request, handler, userDTO = serviceTokenDTO.user!!)
                    }
                } else {
                    createExecutionLog(request, handler, userDTO = serviceTokenDTO.user!!)
                }
            }

            serviceTokenDTO?.device != null -> {
                createExecutionLog(request, handler, deviceDTO = serviceTokenDTO.device!!)
            }

            serviceTokenDTO?.application != null -> {
                createExecutionLog(request, handler, applicationDTO = serviceTokenDTO.application!!)
            }
        }
    }

    private fun createExecutionPackageLog(notFinishedExecutionLog: ExecutionLog, request: HttpServletRequest) {
        val logPackage = ExecutionLogPackage(
            executionLog = notFinishedExecutionLog,
            serviceExecutionStart = dateTimeNow()
        )

        request.setAttribute(EnumRequestAttributes.LOG_ID.name, notFinishedExecutionLog.id)
        request.setAttribute(EnumRequestAttributes.LOG_PACKAGE_ID.name, logPackage.id)

        logPackageRepository.save(logPackage)
    }

    private fun createExecutionLog(
        request: HttpServletRequest,
        handler: HandlerMethod,
        userDTO: IUserDTO? = null,
        deviceDTO: IDeviceDTO? = null,
        applicationDTO: IApplicationDTO? = null
    ) {
        val log = ExecutionLog(
            type = getExecutionType(request.method, request.requestURI),
            endPoint = request.requestURI,
            state = EnumExecutionState.RUNNING,
            methodName = handler.method.name,
            user = userDTO?.email?.let { userRepository.findByEmail(it) },
            device = deviceDTO?.id?.let { deviceRepository.findById(it).get() },
            application = applicationDTO?.id?.let { applicationRepository.findById(it).get() }
        )

        val logPackage = ExecutionLogPackage(
            executionLog = log,
            serviceExecutionStart = LocalDateTime.now()
        )

        request.setAttribute(EnumRequestAttributes.LOG_ID.name, log.id)
        request.setAttribute(EnumRequestAttributes.LOG_PACKAGE_ID.name, logPackage.id)

        logRepository.save(log)
        logPackageRepository.save(logPackage)
    }

    private fun getExecutionType(method: String?, requestURI: String): EnumExecutionType {
        return when {
            requestURI.contains("import") -> IMPORTATION
            requestURI.contains("export") -> EXPORTATION
            requestURI.contains("storage") -> STORAGE
            method == "GET" -> GET
            method == "POST" -> POST
            method == "PUT" -> PUT
            method == "DELETE" -> DELETE
            else -> {
                val message = messageSource.getMessage("execution.log.error.invalid.type", null, Locale.getDefault())
                throw IllegalArgumentException(message)
            }
        }
    }

    fun updateLogAfterCompletion(request: HttpServletRequest) {
        val logId = request.getAttribute(EnumRequestAttributes.LOG_ID.name) as String
        val logPackageId = request.getAttribute(EnumRequestAttributes.LOG_PACKAGE_ID.name) as String
        val requestBody = request.getAttribute(EnumRequestAttributes.REQUEST_BODY_DATA.name) as String?
        val responseBody = request.getAttribute(EnumRequestAttributes.RESPONSE_DATA.name) as String?
        val exception = request.getAttribute(EnumRequestAttributes.REQUEST_EXCEPTION.name) as Exception?

        val log = logRepository.findById(logId).orElseThrow()

        val logPackage = logPackageRepository.findById(logPackageId).orElseThrow()
        logPackage.serviceExecutionEnd = LocalDateTime.now()
        logPackage.requestBody = requestBody
        logPackage.responseBody = responseBody

        val packageExecutions = listOf(IMPORTATION, EXPORTATION, STORAGE)

        if (exception != null) {
            log.state = EnumExecutionState.ERROR
            logPackage.error = exception.stackTraceToString()
        } else if (log.type !in packageExecutions) {
            log.state = EnumExecutionState.FINISHED
        }

        logRepository.save(log)
        logPackageRepository.save(logPackage)
    }

    fun getListExecutionLog(filter: ExecutionLogsFilter, pageInfos: PageInfos): List<ValidatedExecutionLogDTO> {
        return customLogRepository.getListExecutionLog(filter, pageInfos).map(logsServiceMapper::getValidatedExecutionLogDTO)
    }

    fun getCountListExecutionLog(filter: ExecutionLogsFilter): Int {
        return customLogRepository.getCountListExecutionLog(filter)
    }

    fun getListExecutionLogPackage(filter: ExecutionLogsPackageFilter, pageInfos: PageInfos): List<ValidatedExecutionLogPackageDTO> {
        return customLogRepository.getListExecutionLogPackage(filter, pageInfos).map(logsServiceMapper::getValidatedExecutionLogPackageDTO)
    }

    fun getCountListExecutionLogPackage(filter: ExecutionLogsPackageFilter): Int {
        return customLogRepository.getCountListExecutionLogPackage(filter)
    }

    fun updateExecutionLog(id: String, dto: ValidatedUpdatableExecutionLogInfosDTO) {
        val log = logRepository.findById(id).orElseThrow {
            val message = messageSource.getMessage("execution.log.error.not.found", arrayOf(id), Locale.getDefault())
            throw EntityNotFoundException(message)
        }

        log.apply {
            dto.pageSize?.let { pageSize = it }
            dto.lastUpdateDate?.let { lastUpdateDate = it }
            dto.state?.let { state = it }
        }

        logRepository.save(log)
    }

    fun updateExecutionLogPackage(id: String, dto: ValidatedUpdatableExecutionLogPackageInfosDTO) {
        val logPackage = logPackageRepository.findById(id).orElseThrow {
            val message = messageSource.getMessage("execution.log.package.error.not.found", arrayOf(id), Locale.getDefault())
            throw EntityNotFoundException(message)
        }

        logPackage.apply {
            dto.insertedItemsCount?.let { insertedItemsCount = it }
            dto.updatedItemsCount?.let { updatedItemsCount = it }
            dto.allItemsCount?.let { allItemsCount = it }
            dto.clientExecutionStart?.let { clientExecutionStart = it }
            dto.clientExecutionEnd?.let { clientExecutionEnd = it }
            dto.error?.let { error = it }
        }

        logPackageRepository.save(logPackage)
    }

    fun createScheduledTaskStartLog(endPoint: String): Pair<String, String> {
        val log = ExecutionLog(
            type = SCHEDULED_TASK,
            endPoint = "/api/v1/scheduled/task/$endPoint",
            state = EnumExecutionState.RUNNING
        )

        val logPackage = ExecutionLogPackage(
            executionLog = log,
            serviceExecutionStart = dateTimeNow()
        )

        logRepository.save(log)
        logPackageRepository.save(logPackage)

        return Pair(log.id, logPackage.id)
    }

    fun updateScheduledTaskLog(logId: String, logPackageId: String, exception: Exception? = null) {
        val log = logRepository.findById(logId).orElseThrow()

        val logPackage = logPackageRepository.findById(logPackageId).orElseThrow()
        logPackage.serviceExecutionEnd = dateTimeNow()

        if (exception != null) {
            log.state = EnumExecutionState.ERROR
            logPackage.error = exception.stackTraceToString()
        } else {
            log.state = EnumExecutionState.FINISHED
        }

        logRepository.save(log)
        logPackageRepository.save(logPackage)
    }

    fun updateScheduledTaskLogWithAdditionalInfos(logPackageId: String, additionalInfos: StringJoiner) {
        val logPackage = logPackageRepository.findById(logPackageId).orElseThrow()
        logPackage.executionAdditionalInfos = additionalInfos.toString()

        logPackageRepository.save(logPackage)
    }
}