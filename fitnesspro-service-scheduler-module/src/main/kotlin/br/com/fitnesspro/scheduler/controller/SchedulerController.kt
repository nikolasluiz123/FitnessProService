package br.com.fitnesspro.scheduler.controller

import br.com.fitnesspro.authentication.service.SchedulerConfigService
import br.com.fitnesspro.log.enums.EnumRequestAttributes
import br.com.fitnesspro.scheduler.service.SchedulerService
import br.com.fitnesspro.service.communication.dtos.scheduler.ValidatedRecurrentSchedulerDTO
import br.com.fitnesspro.service.communication.dtos.scheduler.ValidatedSchedulerConfigDTO
import br.com.fitnesspro.service.communication.dtos.scheduler.ValidatedSchedulerDTO
import br.com.fitnesspro.service.communication.gson.defaultServiceGSon
import br.com.fitnesspro.service.communication.responses.ValidatedExportationServiceResponse
import br.com.fitnesspro.service.communication.responses.ValidatedFitnessProServiceResponse
import br.com.fitnesspro.service.communication.responses.ValidatedImportationServiceResponse
import br.com.fitnesspro.service.communication.responses.ValidatedPersistenceServiceResponse
import br.com.fitnesspro.shared.communication.constants.EndPointsV1
import br.com.fitnesspro.shared.communication.constants.Timeouts
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.CommonImportFilter
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
    private val schedulerService: SchedulerService,
    private val schedulerConfigService: SchedulerConfigService
) {

    @PostMapping
    @Transactional(timeout = Timeouts.OPERATION_LOW_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun saveScheduler(@RequestBody @Valid schedulerDTO: ValidatedSchedulerDTO): ResponseEntity<ValidatedPersistenceServiceResponse<ValidatedSchedulerDTO>> {
        schedulerService.saveScheduler(schedulerDTO)
        return ResponseEntity.ok(
            ValidatedPersistenceServiceResponse(
                code = HttpStatus.OK.value(),
                success = true,
                savedDTO = schedulerDTO
            )
        )
    }

    @PostMapping(EndPointsV1.SCHEDULER_RECURRENT)
    @Transactional(timeout = Timeouts.OPERATION_LOW_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun saveRecurrentScheduler(@RequestBody @Valid recurrentSchedulerDTO: ValidatedRecurrentSchedulerDTO): ResponseEntity<ValidatedFitnessProServiceResponse> {
        schedulerService.saveRecurrentScheduler(recurrentSchedulerDTO)
        return ResponseEntity.ok(ValidatedFitnessProServiceResponse(code = HttpStatus.OK.value(), success = true))
    }

    @PostMapping(EndPointsV1.SCHEDULER_EXPORT)
    @Transactional(timeout = Timeouts.OPERATION_HIGH_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun saveSchedulerBatch(@RequestBody @Valid schedulerDTOList: List<ValidatedSchedulerDTO>, request: HttpServletRequest): ResponseEntity<ValidatedExportationServiceResponse> {
        schedulerService.saveSchedulerBatch(schedulerDTOList)

        val logId = request.getAttribute(EnumRequestAttributes.LOG_ID.name) as String
        val logPackageId = request.getAttribute(EnumRequestAttributes.LOG_PACKAGE_ID.name) as String
        return ResponseEntity.ok(
            ValidatedExportationServiceResponse(
                executionLogId = logId,
                executionLogPackageId = logPackageId,
                code = HttpStatus.OK.value(),
                success = true
            )
        )
    }

    @PostMapping(EndPointsV1.SCHEDULER_CONFIG_EXPORT)
    @Transactional(timeout = Timeouts.OPERATION_HIGH_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun saveSchedulerConfigBatch(@RequestBody @Valid schedulerConfigDTOList: List<ValidatedSchedulerConfigDTO>, request: HttpServletRequest): ResponseEntity<ValidatedExportationServiceResponse> {
        schedulerConfigService.saveSchedulerConfigBatch(schedulerConfigDTOList)

        val logId = request.getAttribute(EnumRequestAttributes.LOG_ID.name) as String
        val logPackageId = request.getAttribute(EnumRequestAttributes.LOG_PACKAGE_ID.name) as String
        return ResponseEntity.ok(
            ValidatedExportationServiceResponse(
                executionLogId = logId,
                executionLogPackageId = logPackageId,
                code = HttpStatus.OK.value(),
                success = true
            )
        )
    }

    @GetMapping(EndPointsV1.SCHEDULER_IMPORT)
    @Transactional(timeout = Timeouts.OPERATION_MEDIUM_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun importScheduler(@RequestParam filter: String, @RequestParam pageInfos: String, request: HttpServletRequest): ResponseEntity<ValidatedImportationServiceResponse<ValidatedSchedulerDTO>> {
        val defaultGSon = GsonBuilder().defaultServiceGSon()
        val commonImportFilter = defaultGSon.fromJson(filter, CommonImportFilter::class.java)
        val importPageInfos = defaultGSon.fromJson(pageInfos, ImportPageInfos::class.java)

        val values = schedulerService.getSchedulesImport(commonImportFilter, importPageInfos)
        val logId = request.getAttribute(EnumRequestAttributes.LOG_ID.name) as String
        val logPackageId = request.getAttribute(EnumRequestAttributes.LOG_PACKAGE_ID.name) as String
        return ResponseEntity.ok(
            ValidatedImportationServiceResponse(
                executionLogId = logId,
                executionLogPackageId = logPackageId,
                values = values,
                code = HttpStatus.OK.value(),
                success = true,
            )
        )
    }

    @GetMapping(EndPointsV1.SCHEDULER_CONFIG_IMPORT)
    @Transactional(timeout = Timeouts.OPERATION_MEDIUM_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun importSchedulerConfig(@RequestParam filter: String, @RequestParam pageInfos: String, request: HttpServletRequest): ResponseEntity<ValidatedImportationServiceResponse<ValidatedSchedulerConfigDTO>> {
        val defaultGSon = GsonBuilder().defaultServiceGSon()
        val commonImportFilter = defaultGSon.fromJson(filter, CommonImportFilter::class.java)
        val importPageInfos = defaultGSon.fromJson(pageInfos, ImportPageInfos::class.java)

        val values = schedulerConfigService.getSchedulerConfigsImport(commonImportFilter, importPageInfos)
        val logId = request.getAttribute(EnumRequestAttributes.LOG_ID.name) as String
        val logPackageId = request.getAttribute(EnumRequestAttributes.LOG_PACKAGE_ID.name) as String
        return ResponseEntity.ok(
            ValidatedImportationServiceResponse(
                executionLogId = logId,
                executionLogPackageId = logPackageId,
                values = values,
                code = HttpStatus.OK.value(),
                success = true,
            )
        )
    }

}