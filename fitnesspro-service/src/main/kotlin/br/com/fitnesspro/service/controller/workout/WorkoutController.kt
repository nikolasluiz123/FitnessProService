package br.com.fitnesspro.service.controller.workout

import br.com.fitnesspro.service.service.workout.WorkoutService
import br.com.fitnesspro.shared.communication.constants.EndPointsV1
import br.com.fitnesspro.shared.communication.constants.Timeouts
import br.com.fitnesspro.shared.communication.dtos.workout.WorkoutDTO
import br.com.fitnesspro.shared.communication.dtos.workout.WorkoutGroupDTO
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
@RequestMapping(EndPointsV1.WORKOUT_V1)
@Tag(name = "Workout Controller", description = "Operações de Manutenção dos Treinos")
class WorkoutController(
    private val workoutService: WorkoutService
) {

    @PostMapping
    @Transactional(timeout = Timeouts.OPERATION_LOW_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun saveWorkout(@RequestBody @Valid workoutDTO: WorkoutDTO): ResponseEntity<PersistenceServiceResponse<WorkoutDTO>> {
        workoutService.saveWorkout(workoutDTO)
        return ResponseEntity.ok(PersistenceServiceResponse(code = HttpStatus.OK.value(), success = true, savedDTO = workoutDTO))
    }

    @PostMapping(EndPointsV1.WORKOUT_EXPORT)
    @Transactional(timeout = Timeouts.OPERATION_HIGH_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun saveWorkoutBatch(@RequestBody @Valid workoutDTOList: List<WorkoutDTO>): ResponseEntity<PersistenceServiceResponse<WorkoutDTO>> {
        workoutService.saveWorkoutBatch(workoutDTOList)
        return ResponseEntity.ok(PersistenceServiceResponse(code = HttpStatus.OK.value(), success = true))
    }

    @PostMapping(EndPointsV1.WORKOUT_GROUP)
    @Transactional(timeout = Timeouts.OPERATION_LOW_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun saveWorkoutGroup(@RequestBody @Valid workoutGroupDTO: WorkoutGroupDTO): ResponseEntity<PersistenceServiceResponse<WorkoutGroupDTO>> {
        workoutService.saveWorkoutGroup(workoutGroupDTO)
        return ResponseEntity.ok(PersistenceServiceResponse(code = HttpStatus.OK.value(), success = true, savedDTO = workoutGroupDTO))
    }

    @PostMapping(EndPointsV1.WORKOUT_GROUP_EXPORT)
    @Transactional(timeout = Timeouts.OPERATION_HIGH_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun saveWorkoutGroupBatch(@RequestBody @Valid workoutGroupList: List<WorkoutGroupDTO>): ResponseEntity<PersistenceServiceResponse<WorkoutGroupDTO>> {
        workoutService.saveWorkoutGroupBatch(workoutGroupList)
        return ResponseEntity.ok(PersistenceServiceResponse(code = HttpStatus.OK.value(), success = true))
    }
}