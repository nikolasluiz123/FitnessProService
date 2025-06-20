package br.com.fitnesspro.controller.workout

import br.com.fitnesspro.services.workout.ExerciseService
import br.com.fitnesspro.shared.communication.constants.EndPointsV1
import br.com.fitnesspro.shared.communication.constants.Timeouts
import br.com.fitnesspro.shared.communication.dtos.workout.ExerciseDTO
import br.com.fitnesspro.shared.communication.responses.PersistenceServiceResponse
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
@RequestMapping(EndPointsV1.EXERCISE_V1)
@Tag(name = "Exercise Controller", description = "Operações de Manutenção dos Exercícios dos Treinos")
class ExerciseController(
    private val exerciseService: ExerciseService
) {

    @PostMapping
    @Transactional(timeout = Timeouts.OPERATION_LOW_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun saveWorkout(@RequestBody @Valid exerciseDTO: ExerciseDTO): ResponseEntity<PersistenceServiceResponse<ExerciseDTO>> {
        exerciseService.saveExercise(exerciseDTO)
        return ResponseEntity.ok(PersistenceServiceResponse(code = HttpStatus.OK.value(), success = true, savedDTO = exerciseDTO))
    }
}