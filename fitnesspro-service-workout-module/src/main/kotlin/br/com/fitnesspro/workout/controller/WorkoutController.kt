package br.com.fitnesspro.workout.controller

import br.com.fitnesspro.log.enums.EnumRequestAttributes
import br.com.fitnesspro.service.communication.dtos.workout.ValidatedWorkoutDTO
import br.com.fitnesspro.service.communication.dtos.workout.ValidatedWorkoutGroupDTO
import br.com.fitnesspro.service.communication.dtos.workout.ValidatedWorkoutGroupPreDefinitionDTO
import br.com.fitnesspro.service.communication.gson.defaultServiceGSon
import br.com.fitnesspro.service.communication.responses.ValidatedExportationServiceResponse
import br.com.fitnesspro.service.communication.responses.ValidatedImportationServiceResponse
import br.com.fitnesspro.shared.communication.constants.EndPointsV1
import br.com.fitnesspro.shared.communication.constants.Timeouts
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.WorkoutModuleImportFilter
import br.com.fitnesspro.workout.service.WorkoutService
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
@RequestMapping(EndPointsV1.WORKOUT_V1)
@Tag(name = "Workout Controller", description = "Operações de Manutenção dos Treinos")
class WorkoutController(
    private val workoutService: WorkoutService
) {

    @PostMapping(EndPointsV1.WORKOUT_EXPORT)
    @Transactional(timeout = Timeouts.OPERATION_HIGH_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun saveWorkoutBatch(@RequestBody @Valid workoutDTOList: List<ValidatedWorkoutDTO>, request: HttpServletRequest): ResponseEntity<ValidatedExportationServiceResponse> {
        workoutService.saveWorkoutBatch(workoutDTOList)

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

    @GetMapping(EndPointsV1.WORKOUT_IMPORT)
    @Transactional(timeout = Timeouts.OPERATION_MEDIUM_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun importWorkout(@RequestParam filter: String, @RequestParam pageInfos: String, request: HttpServletRequest): ResponseEntity<ValidatedImportationServiceResponse<ValidatedWorkoutDTO>> {
        val defaultGSon = GsonBuilder().defaultServiceGSon()
        val commonImportFilter = defaultGSon.fromJson(filter, WorkoutModuleImportFilter::class.java)
        val importPageInfos = defaultGSon.fromJson(pageInfos, ImportPageInfos::class.java)

        val values = workoutService.getWorkoutsImport(commonImportFilter, importPageInfos)
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

    @PostMapping(EndPointsV1.WORKOUT_GROUP_EXPORT)
    @Transactional(timeout = Timeouts.OPERATION_HIGH_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun saveWorkoutGroupBatch(@RequestBody @Valid workoutGroupList: List<ValidatedWorkoutGroupDTO>, request: HttpServletRequest): ResponseEntity<ValidatedExportationServiceResponse> {
        workoutService.saveWorkoutGroupBatch(workoutGroupList)

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

    @GetMapping(EndPointsV1.WORKOUT_GROUP_IMPORT)
    @Transactional(timeout = Timeouts.OPERATION_MEDIUM_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun importWorkoutGroup(@RequestParam filter: String, @RequestParam pageInfos: String, request: HttpServletRequest): ResponseEntity<ValidatedImportationServiceResponse<ValidatedWorkoutGroupDTO>> {
        val defaultGSon = GsonBuilder().defaultServiceGSon()
        val commonImportFilter = defaultGSon.fromJson(filter, WorkoutModuleImportFilter::class.java)
        val importPageInfos = defaultGSon.fromJson(pageInfos, ImportPageInfos::class.java)

        val values = workoutService.getWorkoutGroupsImport(commonImportFilter, importPageInfos)
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

    @GetMapping(EndPointsV1.WORKOUT_GROUP_PREDEFINITION_IMPORT)
    @Transactional(timeout = Timeouts.OPERATION_MEDIUM_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun importWorkoutGroupPreDefinition(@RequestParam filter: String, @RequestParam pageInfos: String, request: HttpServletRequest): ResponseEntity<ValidatedImportationServiceResponse<ValidatedWorkoutGroupPreDefinitionDTO>> {
        val defaultGSon = GsonBuilder().defaultServiceGSon()
        val commonImportFilter = defaultGSon.fromJson(filter, WorkoutModuleImportFilter::class.java)
        val importPageInfos = defaultGSon.fromJson(pageInfos, ImportPageInfos::class.java)

        val values = workoutService.getWorkoutGroupsPreDefinitionImport(commonImportFilter, importPageInfos)
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

    @PostMapping(EndPointsV1.WORKOUT_GROUP_PREDEFINITION_EXPORT)
    @Transactional(timeout = Timeouts.OPERATION_HIGH_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun saveWorkoutGroupPreDefinitionBatch(@RequestBody @Valid workoutGroupList: List<ValidatedWorkoutGroupPreDefinitionDTO>, request: HttpServletRequest): ResponseEntity<ValidatedExportationServiceResponse> {
        workoutService.saveWorkoutGroupPreDefinitionBatch(workoutGroupList)

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
}