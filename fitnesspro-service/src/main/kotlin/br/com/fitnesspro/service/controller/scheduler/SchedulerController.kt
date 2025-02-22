package br.com.fitnesspro.service.controller.scheduler

import br.com.fitnesspro.core.extensions.dateTimeNow
import br.com.fitnesspro.service.service.scheduler.SchedulerService
import br.com.fitnesspro.shared.communication.constants.EndPointsV1
import br.com.fitnesspro.shared.communication.constants.Timeouts
import br.com.fitnesspro.shared.communication.dtos.scheduler.SchedulerConfigDTO
import br.com.fitnesspro.shared.communication.dtos.scheduler.SchedulerDTO
import br.com.fitnesspro.shared.communication.filter.CommonImportFilter
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.responses.PersistenceServiceResponse
import br.com.fitnesspro.shared.communication.responses.ReadServiceResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(EndPointsV1.SCHEDULER_V1)
@Tag(name = "Scheduler Controller", description = "Operações de Agendamento de Eventos")
class SchedulerController(
    private val schedulerService: SchedulerService
) {

    @PostMapping
    @Transactional(timeout = Timeouts.OPERATION_LOW_TIMEOUT)
    @SecurityRequirement(name = "Bearer Authentication")
    fun saveScheduler(@RequestBody @Valid schedulerDTO: SchedulerDTO): ResponseEntity<PersistenceServiceResponse> {
        schedulerService.saveScheduler(schedulerDTO)
        return ResponseEntity.ok(PersistenceServiceResponse(code = HttpStatus.OK.value(), success = true, transmissionDate = dateTimeNow()))
    }

    @PostMapping(EndPointsV1.SCHEDULER_EXPORT)
    @Transactional(timeout = Timeouts.OPERATION_HIGH_TIMEOUT)
    @SecurityRequirement(name = "Bearer Authentication")
    fun saveSchedulerBatch(@RequestBody @Valid schedulerDTOList: List<SchedulerDTO>): ResponseEntity<PersistenceServiceResponse> {
        schedulerService.saveSchedulerBatch(schedulerDTOList)
        return ResponseEntity.ok(PersistenceServiceResponse(code = HttpStatus.OK.value(), success = true, transmissionDate = dateTimeNow()))
    }

    @PostMapping(EndPointsV1.SCHEDULER_CONFIG)
    @Transactional(timeout = Timeouts.OPERATION_LOW_TIMEOUT)
    fun saveSchedulerConfig(@RequestBody @Valid schedulerConfigDTO: SchedulerConfigDTO): ResponseEntity<PersistenceServiceResponse> {
        schedulerService.saveSchedulerConfig(schedulerConfigDTO)
        return ResponseEntity.ok(PersistenceServiceResponse(code = HttpStatus.OK.value(), success = true, transmissionDate = dateTimeNow()))
    }

    @PostMapping(EndPointsV1.SCHEDULER_CONFIG_EXPORT)
    @Transactional(timeout = Timeouts.OPERATION_HIGH_TIMEOUT)
    @SecurityRequirement(name = "Bearer Authentication")
    fun saveSchedulerConfigBatch(@RequestBody @Valid schedulerConfigDTOList: List<SchedulerConfigDTO>): ResponseEntity<PersistenceServiceResponse> {
        schedulerService.saveSchedulerConfigBatch(schedulerConfigDTOList)
        return ResponseEntity.ok(PersistenceServiceResponse(code = HttpStatus.OK.value(), success = true, transmissionDate = dateTimeNow()))
    }

    @PostMapping(EndPointsV1.SCHEDULER_IMPORT)
    @Transactional(timeout = Timeouts.OPERATION_MEDIUM_TIMEOUT)
    @SecurityRequirement(name = "Bearer Authentication")
    fun importScheduler(@RequestBody filter: CommonImportFilter, pageInfos: ImportPageInfos): ResponseEntity<ReadServiceResponse<SchedulerDTO>> {
        val users = schedulerService.getSchedulesImport(filter, pageInfos)
        return ResponseEntity.ok(ReadServiceResponse(values = users, code = HttpStatus.OK.value(), success = true))
    }

    @PostMapping(EndPointsV1.SCHEDULER_CONFIG_IMPORT)
    @Transactional(timeout = Timeouts.OPERATION_MEDIUM_TIMEOUT)
    @SecurityRequirement(name = "Bearer Authentication")
    fun importSchedulerConfig(@RequestBody filter: CommonImportFilter, pageInfos: ImportPageInfos): ResponseEntity<ReadServiceResponse<SchedulerConfigDTO>> {
        val users = schedulerService.getSchedulerConfigsImport(filter, pageInfos)
        return ResponseEntity.ok(ReadServiceResponse(values = users, code = HttpStatus.OK.value(), success = true))
    }

}