package br.com.fitnesspro.scheduler.controller

import br.com.fitnesspro.scheduler.service.SchedulerService
import br.com.fitnesspro.service.communication.dtos.scheduler.ValidatedRecurrentSchedulerDTO
import br.com.fitnesspro.service.communication.dtos.scheduler.ValidatedSchedulerDTO
import br.com.fitnesspro.service.communication.responses.ValidatedFitnessProServiceResponse
import br.com.fitnesspro.service.communication.responses.ValidatedPersistenceServiceResponse
import br.com.fitnesspro.shared.communication.constants.EndPointsV1
import br.com.fitnesspro.shared.communication.constants.Timeouts
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
    private val schedulerService: SchedulerService,
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

}