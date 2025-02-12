package br.com.fitnesspro.controller.scheduler

import br.com.fitnesspro.controller.common.constants.EndPointsV1
import br.com.fitnesspro.controller.common.constants.Timeouts
import br.com.fitnesspro.controller.common.responses.PersistenceServiceResponse
import br.com.fitnesspro.controller.common.responses.ReadServiceResponse
import br.com.fitnesspro.dto.scheduler.SchedulerConfigDTO
import br.com.fitnesspro.dto.scheduler.SchedulerDTO
import br.com.fitnesspro.repository.common.filter.CommonImportFilter
import br.com.fitnesspro.repository.common.paging.ImportPageInfos
import br.com.fitnesspro.service.scheduler.SchedulerService
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
    @Transactional(timeout = Timeouts.LOW_TIMEOUT)
    @SecurityRequirement(name = "Bearer Authentication")
    fun saveScheduler(@RequestBody @Valid schedulerDTO: SchedulerDTO): ResponseEntity<PersistenceServiceResponse> {
        schedulerService.saveScheduler(schedulerDTO)
        return ResponseEntity.ok(PersistenceServiceResponse(code = HttpStatus.OK.value(), success = true))
    }

    @PostMapping(EndPointsV1.SCHEDULER_EXPORT)
    @Transactional(timeout = Timeouts.HIGH_TIMEOUT)
    @SecurityRequirement(name = "Bearer Authentication")
    fun saveSchedulerBatch(@RequestBody @Valid schedulerDTOList: List<SchedulerDTO>): ResponseEntity<PersistenceServiceResponse> {
        schedulerService.saveSchedulerBatch(schedulerDTOList)
        return ResponseEntity.ok(PersistenceServiceResponse(code = HttpStatus.OK.value(), success = true))
    }

    @PostMapping(EndPointsV1.SCHEDULER_CONFIG)
    @Transactional(timeout = Timeouts.LOW_TIMEOUT)
    @SecurityRequirement(name = "Bearer Authentication")
    fun saveSchedulerConfig(@RequestBody @Valid schedulerConfigDTO: SchedulerConfigDTO): ResponseEntity<PersistenceServiceResponse> {
        schedulerService.saveSchedulerConfig(schedulerConfigDTO)
        return ResponseEntity.ok(PersistenceServiceResponse(code = HttpStatus.OK.value(), success = true))
    }

    @PostMapping(EndPointsV1.SCHEDULER_CONFIG_EXPORT)
    @Transactional(timeout = Timeouts.HIGH_TIMEOUT)
    @SecurityRequirement(name = "Bearer Authentication")
    fun saveSchedulerConfigBatch(@RequestBody @Valid schedulerConfigDTOList: List<SchedulerConfigDTO>): ResponseEntity<PersistenceServiceResponse> {
        schedulerService.saveSchedulerConfigBatch(schedulerConfigDTOList)
        return ResponseEntity.ok(PersistenceServiceResponse(code = HttpStatus.OK.value(), success = true))
    }

    @PostMapping(EndPointsV1.SCHEDULER_IMPORT)
    @Transactional(timeout = Timeouts.MEDIUM_TIMEOUT)
    @SecurityRequirement(name = "Bearer Authentication")
    fun importScheduler(@RequestBody filter: CommonImportFilter, pageInfos: ImportPageInfos): ResponseEntity<ReadServiceResponse<SchedulerDTO>> {
        val users = schedulerService.getSchedulesImport(filter, pageInfos)
        return ResponseEntity.ok(ReadServiceResponse(values = users, code = HttpStatus.OK.value(), success = true))
    }

    @PostMapping(EndPointsV1.SCHEDULER_CONFIG_IMPORT)
    @Transactional(timeout = Timeouts.MEDIUM_TIMEOUT)
    @SecurityRequirement(name = "Bearer Authentication")
    fun importSchedulerConfig(@RequestBody filter: CommonImportFilter, pageInfos: ImportPageInfos): ResponseEntity<ReadServiceResponse<SchedulerConfigDTO>> {
        val users = schedulerService.getSchedulerConfigsImport(filter, pageInfos)
        return ResponseEntity.ok(ReadServiceResponse(values = users, code = HttpStatus.OK.value(), success = true))
    }

}