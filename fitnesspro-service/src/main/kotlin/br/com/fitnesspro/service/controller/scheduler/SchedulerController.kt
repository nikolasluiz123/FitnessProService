package br.com.fitnesspro.service.controller.scheduler

import br.com.fitnesspro.service.config.gson.defaultGSon
import br.com.fitnesspro.service.config.request.EnumRequestAttributes
import br.com.fitnesspro.service.service.scheduler.SchedulerService
import br.com.fitnesspro.shared.communication.constants.EndPointsV1
import br.com.fitnesspro.shared.communication.constants.Timeouts
import br.com.fitnesspro.shared.communication.dtos.scheduler.SchedulerConfigDTO
import br.com.fitnesspro.shared.communication.dtos.scheduler.SchedulerDTO
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.CommonImportFilter
import br.com.fitnesspro.shared.communication.responses.ExportationServiceResponse
import br.com.fitnesspro.shared.communication.responses.ImportationServiceResponse
import br.com.fitnesspro.shared.communication.responses.PersistenceServiceResponse
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
@RequestMapping(EndPointsV1.SCHEDULER_V1)
@Tag(name = "Scheduler Controller", description = "Operações de Agendamento de Eventos")
class SchedulerController(
    private val schedulerService: SchedulerService
) {

    @PostMapping
    @Transactional(timeout = Timeouts.OPERATION_LOW_TIMEOUT)
    @SecurityRequirement(name = "Bearer Authentication")
    fun saveScheduler(@RequestBody @Valid schedulerDTO: SchedulerDTO): ResponseEntity<PersistenceServiceResponse<SchedulerDTO>> {
        schedulerService.saveScheduler(schedulerDTO)
        return ResponseEntity.ok(PersistenceServiceResponse(code = HttpStatus.OK.value(), success = true, savedDTO = schedulerDTO))
    }

    @PostMapping(EndPointsV1.SCHEDULER_EXPORT)
    @Transactional(timeout = Timeouts.OPERATION_HIGH_TIMEOUT)
    @SecurityRequirement(name = "Bearer Authentication")
    fun saveSchedulerBatch(@RequestBody @Valid schedulerDTOList: List<SchedulerDTO>, request: HttpServletRequest): ResponseEntity<ExportationServiceResponse> {
        schedulerService.saveSchedulerBatch(schedulerDTOList)

        val logId = request.getAttribute(EnumRequestAttributes.LOG_ID.name) as String
        val logPackageId = request.getAttribute(EnumRequestAttributes.LOG_PACKAGE_ID.name) as String
        return ResponseEntity.ok(
            ExportationServiceResponse(
                executionLogId = logId,
                executionLogPackageId = logPackageId,
                code = HttpStatus.OK.value(),
                success = true
            )
        )
    }

    @PostMapping(EndPointsV1.SCHEDULER_CONFIG)
    @Transactional(timeout = Timeouts.OPERATION_LOW_TIMEOUT)
    @SecurityRequirement(name = "Bearer Authentication")
    fun saveSchedulerConfig(@RequestBody @Valid schedulerConfigDTO: SchedulerConfigDTO): ResponseEntity<PersistenceServiceResponse<SchedulerConfigDTO>> {
        schedulerService.saveSchedulerConfig(schedulerConfigDTO)
        return ResponseEntity.ok(PersistenceServiceResponse(code = HttpStatus.OK.value(), success = true, savedDTO = schedulerConfigDTO))
    }

    @PostMapping(EndPointsV1.SCHEDULER_CONFIG_EXPORT)
    @Transactional(timeout = Timeouts.OPERATION_HIGH_TIMEOUT)
    @SecurityRequirement(name = "Bearer Authentication")
    fun saveSchedulerConfigBatch(@RequestBody @Valid schedulerConfigDTOList: List<SchedulerConfigDTO>, request: HttpServletRequest): ResponseEntity<ExportationServiceResponse> {
        schedulerService.saveSchedulerConfigBatch(schedulerConfigDTOList)

        val logId = request.getAttribute(EnumRequestAttributes.LOG_ID.name) as String
        val logPackageId = request.getAttribute(EnumRequestAttributes.LOG_PACKAGE_ID.name) as String
        return ResponseEntity.ok(
            ExportationServiceResponse(
                executionLogId = logId,
                executionLogPackageId = logPackageId,
                code = HttpStatus.OK.value(),
                success = true
            )
        )
    }

    @GetMapping(EndPointsV1.SCHEDULER_IMPORT)
    @Transactional(timeout = Timeouts.OPERATION_MEDIUM_TIMEOUT)
    @SecurityRequirement(name = "Bearer Authentication")
    fun importScheduler(@RequestParam filter: String, @RequestParam pageInfos: String, request: HttpServletRequest): ResponseEntity<ImportationServiceResponse<SchedulerDTO>> {
        val defaultGSon = GsonBuilder().defaultGSon()
        val commonImportFilter = defaultGSon.fromJson(filter, CommonImportFilter::class.java)
        val importPageInfos = defaultGSon.fromJson(pageInfos, ImportPageInfos::class.java)

        val values = schedulerService.getSchedulesImport(commonImportFilter, importPageInfos)
        val logId = request.getAttribute(EnumRequestAttributes.LOG_ID.name) as String
        val logPackageId = request.getAttribute(EnumRequestAttributes.LOG_PACKAGE_ID.name) as String
        return ResponseEntity.ok(
            ImportationServiceResponse(
                executionLogId = logId,
                executionLogPackageId = logPackageId,
                values = values,
                code = HttpStatus.OK.value(),
                success = true,
            )
        )
    }

    @GetMapping(EndPointsV1.SCHEDULER_CONFIG_IMPORT)
    @Transactional(timeout = Timeouts.OPERATION_MEDIUM_TIMEOUT)
    @SecurityRequirement(name = "Bearer Authentication")
    fun importSchedulerConfig(@RequestParam filter: String, @RequestParam pageInfos: String, request: HttpServletRequest): ResponseEntity<ImportationServiceResponse<SchedulerConfigDTO>> {
        val defaultGSon = GsonBuilder().defaultGSon()
        val commonImportFilter = defaultGSon.fromJson(filter, CommonImportFilter::class.java)
        val importPageInfos = defaultGSon.fromJson(pageInfos, ImportPageInfos::class.java)

        val values = schedulerService.getSchedulerConfigsImport(commonImportFilter, importPageInfos)
        val logId = request.getAttribute(EnumRequestAttributes.LOG_ID.name) as String
        val logPackageId = request.getAttribute(EnumRequestAttributes.LOG_PACKAGE_ID.name) as String
        return ResponseEntity.ok(
            ImportationServiceResponse(
                executionLogId = logId,
                executionLogPackageId = logPackageId,
                values = values,
                code = HttpStatus.OK.value(),
                success = true,
            )
        )
    }

}