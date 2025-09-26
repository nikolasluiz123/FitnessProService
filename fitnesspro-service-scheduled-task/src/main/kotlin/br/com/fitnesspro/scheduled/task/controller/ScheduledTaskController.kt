package br.com.fitnesspro.scheduled.task.controller

import br.com.fitnesspro.scheduled.task.service.ScheduledTaskService
import br.com.fitnesspro.service.communication.dtos.scheduledtask.ValidatedScheduledTaskDTO
import br.com.fitnesspro.service.communication.responses.ValidatedPersistenceServiceResponse
import br.com.fitnesspro.service.communication.responses.ValidatedReadServiceResponse
import br.com.fitnesspro.shared.communication.constants.EndPointsV1
import br.com.fitnesspro.shared.communication.constants.Timeouts
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
    fun saveScheduledTask(@RequestBody @Valid validatedScheduledTaskDTO: ValidatedScheduledTaskDTO): ResponseEntity<ValidatedPersistenceServiceResponse<ValidatedScheduledTaskDTO>> {
        scheduledTaskService.saveScheduledTask(validatedScheduledTaskDTO)
        return ResponseEntity.ok(
            ValidatedPersistenceServiceResponse(
                code = HttpStatus.OK.value(),
                success = true,
                savedDTO = validatedScheduledTaskDTO
            )
        )
    }

    @GetMapping
    @Transactional(timeout = Timeouts.OPERATION_MEDIUM_TIMEOUT, rollbackFor = [Exception::class], readOnly = true)
    @SecurityRequirement(name = "Bearer Authentication")
    fun getListScheduledTask(): ResponseEntity<ValidatedReadServiceResponse<ValidatedScheduledTaskDTO>> {
        val result = scheduledTaskService.getListScheduledTask()
        return ResponseEntity.ok(
            ValidatedReadServiceResponse(
                values = result,
                code = HttpStatus.OK.value(),
                success = true
            )
        )
    }

}