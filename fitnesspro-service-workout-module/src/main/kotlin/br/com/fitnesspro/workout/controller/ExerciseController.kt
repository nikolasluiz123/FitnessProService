package br.com.fitnesspro.workout.controller

import br.com.fitnesspro.log.enums.EnumRequestAttributes
import br.com.fitnesspro.service.communication.dtos.workout.*
import br.com.fitnesspro.service.communication.gson.defaultServiceGSon
import br.com.fitnesspro.service.communication.responses.ValidatedExportationServiceResponse
import br.com.fitnesspro.service.communication.responses.ValidatedImportationServiceResponse
import br.com.fitnesspro.shared.communication.constants.EndPointsV1
import br.com.fitnesspro.shared.communication.constants.Timeouts
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.WorkoutModuleImportFilter
import br.com.fitnesspro.workout.service.ExerciseService
import br.com.fitnesspro.workout.service.VideoService
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
@RequestMapping(EndPointsV1.EXERCISE_V1)
@Tag(name = "Exercise Controller", description = "Operações de Manutenção dos Exercícios dos Treinos")
class ExerciseController(
    private val exerciseService: ExerciseService,
    private val videoService: VideoService
) {

    @PostMapping(EndPointsV1.EXERCISE_EXPORT)
    @Transactional(timeout = Timeouts.OPERATION_HIGH_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun saveExerciseBatch(@RequestBody @Valid exerciseDTOs: List<ValidatedExerciseDTO>, request: HttpServletRequest): ResponseEntity<ValidatedExportationServiceResponse> {
        exerciseService.saveExerciseBatch(exerciseDTOs)

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

    @PostMapping(EndPointsV1.EXERCISE_EXECUTION_EXPORT)
    @Transactional(timeout = Timeouts.OPERATION_HIGH_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun saveExerciseExecutionBatch(@RequestBody @Valid exerciseDTOs: List<ValidatedExerciseExecutionDTO>, request: HttpServletRequest): ResponseEntity<ValidatedExportationServiceResponse> {
        exerciseService.saveExerciseExecutionBatch(exerciseDTOs)

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

    @GetMapping(EndPointsV1.EXERCISE_IMPORT)
    @Transactional(timeout = Timeouts.OPERATION_MEDIUM_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun importExercise(@RequestParam filter: String, @RequestParam pageInfos: String, request: HttpServletRequest): ResponseEntity<ValidatedImportationServiceResponse<ValidatedExerciseDTO>> {
        val defaultGSon = GsonBuilder().defaultServiceGSon()
        val commonImportFilter = defaultGSon.fromJson(filter, WorkoutModuleImportFilter::class.java)
        val importPageInfos = defaultGSon.fromJson(pageInfos, ImportPageInfos::class.java)

        val values = exerciseService.getExercisesImport(commonImportFilter, importPageInfos)
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

    @GetMapping(EndPointsV1.EXERCISE_EXECUTION_IMPORT)
    @Transactional(timeout = Timeouts.OPERATION_MEDIUM_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun importExerciseExecution(@RequestParam filter: String, @RequestParam pageInfos: String, request: HttpServletRequest): ResponseEntity<ValidatedImportationServiceResponse<ValidatedExerciseExecutionDTO>> {
        val defaultGSon = GsonBuilder().defaultServiceGSon()
        val importFilter = defaultGSon.fromJson(filter, WorkoutModuleImportFilter::class.java)
        val importPageInfos = defaultGSon.fromJson(pageInfos, ImportPageInfos::class.java)

        val values = exerciseService.getExercisesExecutionImport(importFilter, importPageInfos)
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

    @GetMapping(EndPointsV1.EXERCISE_VIDEO_IMPORT)
    @Transactional(timeout = Timeouts.OPERATION_MEDIUM_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun importVideoExercise(@RequestParam filter: String, @RequestParam pageInfos: String, request: HttpServletRequest): ResponseEntity<ValidatedImportationServiceResponse<ValidatedVideoExerciseDTO>> {
        val defaultGSon = GsonBuilder().defaultServiceGSon()
        val commonImportFilter = defaultGSon.fromJson(filter, WorkoutModuleImportFilter::class.java)
        val importPageInfos = defaultGSon.fromJson(pageInfos, ImportPageInfos::class.java)

        val values = videoService.getVideoExercisesImport(commonImportFilter, importPageInfos)
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

    @GetMapping(EndPointsV1.EXERCISE_EXECUTION_VIDEO_IMPORT)
    @Transactional(timeout = Timeouts.OPERATION_MEDIUM_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun importVideoExerciseExecution(@RequestParam filter: String, @RequestParam pageInfos: String, request: HttpServletRequest): ResponseEntity<ValidatedImportationServiceResponse<ValidatedVideoExerciseExecutionDTO>> {
        val defaultGSon = GsonBuilder().defaultServiceGSon()
        val commonImportFilter = defaultGSon.fromJson(filter, WorkoutModuleImportFilter::class.java)
        val importPageInfos = defaultGSon.fromJson(pageInfos, ImportPageInfos::class.java)

        val values = videoService.getVideoExercisesExecutionImport(commonImportFilter, importPageInfos)
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

    @PostMapping(EndPointsV1.EXERCISE_VIDEO_EXPORT)
    @Transactional(timeout = Timeouts.OPERATION_HIGH_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun saveExerciseVideosBatch(@RequestBody @Valid exerciseVideoDTOs: List<ValidatedVideoExerciseDTO>, request: HttpServletRequest): ResponseEntity<ValidatedExportationServiceResponse> {
        videoService.saveExerciseVideoBatch(exerciseVideoDTOs)

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

    @PostMapping(EndPointsV1.EXERCISE_EXECUTION_VIDEO_EXPORT)
    @Transactional(timeout = Timeouts.OPERATION_HIGH_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun saveExerciseExecutionVideosBatch(@RequestBody @Valid videoDTOs: List<ValidatedVideoExerciseExecutionDTO>, request: HttpServletRequest): ResponseEntity<ValidatedExportationServiceResponse> {
        videoService.saveExerciseExecutionVideoBatch(videoDTOs)

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

    @PostMapping(EndPointsV1.EXERCISE_PREDEFINITION_EXPORT)
    @Transactional(timeout = Timeouts.OPERATION_HIGH_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun saveExercisePreDefinitionBatch(@RequestBody @Valid exerciseDTOs: List<ValidatedExercisePreDefinitionDTO>, request: HttpServletRequest): ResponseEntity<ValidatedExportationServiceResponse> {
        exerciseService.saveExercisePreDefinitionBatch(exerciseDTOs)

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

    @PostMapping(EndPointsV1.EXERCISE_PREDEFINITION_VIDEO_EXPORT)
    @Transactional(timeout = Timeouts.OPERATION_HIGH_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun saveExercisePreDefinitionVideosBatch(@RequestBody @Valid exerciseVideoDTOs: List<ValidatedVideoExercisePreDefinitionDTO>, request: HttpServletRequest): ResponseEntity<ValidatedExportationServiceResponse> {
        videoService.saveExercisePreDefinitionVideosBatch(exerciseVideoDTOs)

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

    @GetMapping(EndPointsV1.EXERCISE_PREDEFINITION_IMPORT)
    @Transactional(timeout = Timeouts.OPERATION_MEDIUM_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun importExercisePreDefinition(@RequestParam filter: String, @RequestParam pageInfos: String, request: HttpServletRequest): ResponseEntity<ValidatedImportationServiceResponse<ValidatedExercisePreDefinitionDTO>> {
        val defaultGSon = GsonBuilder().defaultServiceGSon()
        val importFilter = defaultGSon.fromJson(filter, WorkoutModuleImportFilter::class.java)
        val importPageInfos = defaultGSon.fromJson(pageInfos, ImportPageInfos::class.java)

        val values = exerciseService.getExercisesPredefinitionImport(importFilter, importPageInfos)
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

    @GetMapping(EndPointsV1.EXERCISE_PREDEFINITION_VIDEO_IMPORT)
    @Transactional(timeout = Timeouts.OPERATION_MEDIUM_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun importVideoExercisePreDefinition(@RequestParam filter: String, @RequestParam pageInfos: String, request: HttpServletRequest): ResponseEntity<ValidatedImportationServiceResponse<ValidatedVideoExercisePreDefinitionDTO>> {
        val defaultGSon = GsonBuilder().defaultServiceGSon()
        val commonImportFilter = defaultGSon.fromJson(filter, WorkoutModuleImportFilter::class.java)
        val importPageInfos = defaultGSon.fromJson(pageInfos, ImportPageInfos::class.java)

        val values = videoService.getVideoExercisesPreDefinitionImport(commonImportFilter, importPageInfos)
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