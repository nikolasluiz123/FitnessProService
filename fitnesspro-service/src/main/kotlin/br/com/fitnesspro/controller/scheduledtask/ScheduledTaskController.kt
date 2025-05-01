package br.com.fitnesspro.controller.scheduledtask

import br.com.fitnesspro.services.scheduledtask.ScheduledTaskService
import br.com.fitnesspro.shared.communication.constants.EndPointsV1
import br.com.fitnesspro.shared.communication.constants.Timeouts
import br.com.fitnesspro.shared.communication.dtos.scheduledtask.ScheduledTaskDTO
import br.com.fitnesspro.shared.communication.responses.PersistenceServiceResponse
import br.com.fitnesspro.shared.communication.responses.ReadServiceResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(EndPointsV1.SCHEDULED_TASK_V1)
@Tag(name = "Scheduled Task Controller", description = "Manutenção de Serviços Agendados")
class ScheduledTaskController(
    private val scheduledTaskService: ScheduledTaskService
) {

    @PostMapping
    @Transactional(timeout = Timeouts.OPERATION_LOW_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun saveScheduledTask(@RequestBody @Valid scheduledTaskDTO: ScheduledTaskDTO): ResponseEntity<PersistenceServiceResponse<ScheduledTaskDTO>> {
        scheduledTaskService.saveScheduledTask(scheduledTaskDTO)
        return ResponseEntity.ok(PersistenceServiceResponse(code = HttpStatus.OK.value(), success = true, savedDTO = scheduledTaskDTO))
    }

    @GetMapping
    @Transactional(timeout = Timeouts.OPERATION_MEDIUM_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun getListScheduledTask(): ResponseEntity<ReadServiceResponse<ScheduledTaskDTO>> {
        val result = scheduledTaskService.getListScheduledTask()
        return ResponseEntity.ok(ReadServiceResponse(values = result, code = HttpStatus.OK.value(), success = true))
    }

}