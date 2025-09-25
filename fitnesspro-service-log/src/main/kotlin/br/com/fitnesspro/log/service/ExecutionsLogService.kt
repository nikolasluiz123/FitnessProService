package br.com.fitnesspro.log.service

import br.com.fitnesspro.authentication.repository.auditable.IDeviceRepository
import br.com.fitnesspro.authentication.repository.auditable.IUserRepository
import br.com.fitnesspro.authentication.repository.jpa.IApplicationRepository
import br.com.fitnesspro.authentication.service.TokenService
import br.com.fitnesspro.core.extensions.dateTimeNow
import br.com.fitnesspro.log.enums.EnumRequestAttributes
import br.com.fitnesspro.log.repository.jpa.ICustomExecutionsLogRepository
import br.com.fitnesspro.log.repository.jpa.IExecutionsLogRepository
import br.com.fitnesspro.log.repository.jpa.logpackage.ICustomExecutionsLogPackageRepository
import br.com.fitnesspro.log.repository.jpa.logpackage.IExecutionsLogPackageRepository
import br.com.fitnesspro.log.repository.jpa.subpackage.ICustomExecutionsLogSubPackageRepository
import br.com.fitnesspro.log.repository.jpa.subpackage.IExecutionsLogSupPackageRepository
import br.com.fitnesspro.log.service.mappers.LogsServiceMapper
import br.com.fitnesspro.models.logs.ExecutionLog
import br.com.fitnesspro.models.logs.ExecutionLogPackage
import br.com.fitnesspro.models.logs.ExecutionLogSubPackage
import br.com.fitnesspro.service.communication.dtos.annotation.EntityReference
import br.com.fitnesspro.service.communication.dtos.logs.*
import br.com.fitnesspro.service.communication.gson.defaultServiceGSon
import br.com.fitnesspro.shared.communication.dtos.general.interfaces.IUserDTO
import br.com.fitnesspro.shared.communication.dtos.serviceauth.interfaces.IApplicationDTO
import br.com.fitnesspro.shared.communication.dtos.serviceauth.interfaces.IDeviceDTO
import br.com.fitnesspro.shared.communication.enums.execution.EnumExecutionState
import br.com.fitnesspro.shared.communication.enums.execution.EnumExecutionType
import br.com.fitnesspro.shared.communication.enums.execution.EnumExecutionType.*
import br.com.fitnesspro.shared.communication.paging.PageInfos
import br.com.fitnesspro.shared.communication.query.filter.ExecutionLogsFilter
import br.com.fitnesspro.shared.communication.query.filter.ExecutionLogsPackageFilter
import com.google.gson.GsonBuilder
import jakarta.persistence.EntityNotFoundException
import jakarta.servlet.http.HttpServletRequest
import org.springframework.context.MessageSource
import org.springframework.stereotype.Service
import org.springframework.web.method.HandlerMethod
import java.time.LocalDateTime
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.jvmErasure

@Service
class ExecutionsLogService(
    private val logRepository: IExecutionsLogRepository,
    private val logPackageRepository: IExecutionsLogPackageRepository,
    private val logSubPackageRepository: IExecutionsLogSupPackageRepository,
    private val customLogRepository: ICustomExecutionsLogRepository,
    private val customSubPackageRepository: ICustomExecutionsLogSubPackageRepository,
    private val customExecutionsLogPackageRepository: ICustomExecutionsLogPackageRepository,
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
        val syncDTOClass = request.getAttribute(EnumRequestAttributes.SYNC_DTO_CLASS.name) as KClass<*>?
        val syncDTOJSON = request.getAttribute(EnumRequestAttributes.SYNC_DTO_JSON.name) as String?

        val log = logRepository.findById(logId).orElseThrow()

        val logPackage = logPackageRepository.findById(logPackageId).orElseThrow()
        logPackage.serviceExecutionEnd = LocalDateTime.now()
        logPackage.requestBody = requestBody
        logPackage.responseBody = responseBody

        val packageExecutions = listOf(IMPORTATION, EXPORTATION, STORAGE)

        createExecutionSubPackageLog(request, log, syncDTOJSON, syncDTOClass, logPackage)

        if (exception != null) {
            log.state = EnumExecutionState.ERROR
            logPackage.error = exception.stackTraceToString()
        } else if (log.type !in packageExecutions) {
            log.state = EnumExecutionState.FINISHED
        }

        logRepository.save(log)
        logPackageRepository.save(logPackage)
    }

    @Suppress("UNCHECKED_CAST")
    private fun createExecutionSubPackageLog(
        request: HttpServletRequest,
        log: ExecutionLog,
        syncDTOJSON: String?,
        syncDTOClass: KClass<*>?,
        logPackage: ExecutionLogPackage
    ) {
        if (log.type in listOf(IMPORTATION, EXPORTATION)) {
            val gson = GsonBuilder().defaultServiceGSon()
            val syncDTO = gson.fromJson(syncDTOJSON, syncDTOClass!!.java)
            val listProperties = syncDTOClass.memberProperties.filter { property ->
                property.returnType.jvmErasure == List::class && property.hasAnnotation<EntityReference>()
            }

            if (log.type == IMPORTATION) {
                val subPackages = listProperties.map { property ->
                    property as KProperty1<Any, *>

                    val value = property.get(syncDTO) as List<*>
                    val valueAsJSON = gson.toJson(value)
                    val kbSize = valueAsJSON.toByteArray(Charsets.UTF_8).size / 1024L

                    ExecutionLogSubPackage(
                        executionLogPackage = logPackage,
                        entityName = property.findAnnotation<EntityReference>()?.entitySimpleName!!,
                        allItemsCount = value.size,
                        responseBody = valueAsJSON,
                        kbSize = kbSize
                    )
                }

                logSubPackageRepository.saveAll(subPackages)
            } else {
                val subPackages = listProperties.map { property ->
                    property as KProperty1<Any, *>

                    val value = property.get(syncDTO) as List<*>
                    val valueAsJSON = gson.toJson(value)
                    val kbSize = valueAsJSON.toByteArray(Charsets.UTF_8).size / 1024L

                    ExecutionLogSubPackage(
                        executionLogPackage = logPackage,
                        entityName = property.findAnnotation<EntityReference>()?.entitySimpleName!!,
                        allItemsCount = value.size,
                        requestBody = valueAsJSON,
                        kbSize = kbSize
                    )
                }

                logSubPackageRepository.saveAll(subPackages)
            }
        }
    }

    fun getListExecutionLog(filter: ExecutionLogsFilter, pageInfos: PageInfos): List<ValidatedExecutionLogDTO> {
        return customLogRepository.getListExecutionLog(filter, pageInfos).map {
            val dto = logsServiceMapper.getValidatedExecutionLogDTO(it)
            val subPackageCalculatedInformation = customSubPackageRepository.calculateSubPackageInformation(logId = it.id)
            val processingTime = customExecutionsLogPackageRepository.getExecutionProcessingTime(it.id)

            dto.insertedItemsCount = subPackageCalculatedInformation?.insertedItemsCount?.toInt()
            dto.updatedItemsCount = subPackageCalculatedInformation?.updatedItemsCount?.toInt()
            dto.allItemsCount = subPackageCalculatedInformation?.allItemsCount?.toInt()
            dto.kbSize = subPackageCalculatedInformation?.kbSize ?: 0
            dto.serviceProcessingDuration = processingTime.service
            dto.clientProcessingDuration = processingTime.client

            dto
        }
    }

    fun getCountListExecutionLog(filter: ExecutionLogsFilter): Int {
        return customLogRepository.getCountListExecutionLog(filter)
    }

    fun getListExecutionLogPackage(filter: ExecutionLogsPackageFilter, pageInfos: PageInfos): List<ValidatedExecutionLogPackageDTO> {
        return customLogRepository.getListExecutionLogPackage(filter, pageInfos).map {
            val dto = logsServiceMapper.getValidatedExecutionLogPackageDTO(it)
            val result = customSubPackageRepository.calculateSubPackageInformation(packageId = it.id)

            dto.insertedItemsCount = result?.insertedItemsCount?.toInt()
            dto.updatedItemsCount = result?.updatedItemsCount?.toInt()
            dto.allItemsCount = result?.allItemsCount?.toInt()
            dto.kbSize = result?.kbSize ?: 0

            dto
        }
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

    fun updateExecutionLogSubPackage(
        executionLogPackageId: String,
        dto: ValidatedUpdatableExecutionLogSubPackageInfosDTO
    ) {
        val subPackages = customSubPackageRepository.findSubPackagesByPackageId(executionLogPackageId)

        dto.entityCounts.forEach { mapCount ->
            subPackages
                .first { it.entityName == mapCount.key }
                .apply {
                   insertedItemsCount = mapCount.value.insertedItemsCount
                   updatedItemsCount = mapCount.value.updatedItemsCount
                }
        }

        dto.lastUpdateDateMap.forEach { mapUpdateDate ->
            subPackages
                .first { it.entityName == mapUpdateDate.key }
                .apply {
                    lastUpdateDate = mapUpdateDate.value
                }
        }

        logSubPackageRepository.saveAll(subPackages)
    }
}