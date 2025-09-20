package br.com.fitnesspro.workout.service.mappers

import br.com.fitnesspro.models.workout.ExerciseExecution
import br.com.fitnesspro.models.workout.health.*
import br.com.fitnesspro.service.communication.dtos.workout.health.*
import br.com.fitnesspro.service.communication.extensions.getOrThrowDefaultException
import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.health.*
import br.com.fitnesspro.workout.repository.auditable.IExerciseExecutionRepository
import br.com.fitnesspro.workout.repository.auditable.health.*
import org.springframework.context.MessageSource
import org.springframework.stereotype.Service

@Service
class HealthConnectServiceMapper(
    private val metadataRepository: IHealthConnectMetadataRepository,
    private val stepsRepository: IHealthConnectStepsRepository,
    private val caloriesBurnedRepository: IHealthConnectCaloriesBurnedRepository,
    private val heartRateRepository: IHealthConnectHeartRateRepository,
    private val heartRateSamplesRepository: IHealthConnectHeartRateSamplesRepository,
    private val sleepSessionRepository: IHealthConnectSleepSessionRepository,
    private val sleepStagesRepository: IHealthConnectSleepStagesRepository,
    private val sleepSessionExerciseExecutionRepository: ISleepSessionExerciseExecutionRepository,
    private val exerciseExecutionRepository: IExerciseExecutionRepository,
    private val messageSource: MessageSource
) {

    fun getHealthConnectMetadata(dto: IHealthConnectMetadataDTO): HealthConnectMetadata {
        val metadata = dto.id?.let { metadataRepository.findById(it) }

        return when {
            metadata?.isPresent == true -> {
                metadata.get().copy(
                    active = dto.active,
                    dataOriginPackage = dto.dataOriginPackage,
                    lastModifiedTime = dto.lastModifiedTime,
                    clientRecordId = dto.clientRecordId,
                    deviceManufacturer = dto.deviceManufacturer,
                    deviceModel = dto.deviceModel,
                    recordingMethod = dto.recordingMethod
                )
            }
            else -> {
                HealthConnectMetadata(
                    id = dto.id!!,
                    active = dto.active,
                    dataOriginPackage = dto.dataOriginPackage,
                    lastModifiedTime = dto.lastModifiedTime,
                    clientRecordId = dto.clientRecordId,
                    deviceManufacturer = dto.deviceManufacturer,
                    deviceModel = dto.deviceModel,
                    recordingMethod = dto.recordingMethod
                )
            }
        }
    }

    fun getHealthConnectMetadataDTO(model: HealthConnectMetadata): ValidatedHealthConnectMetadataDTO {
        return ValidatedHealthConnectMetadataDTO(
            id = model.id,
            creationDate = model.creationDate,
            updateDate = model.updateDate,
            active = model.active,
            dataOriginPackage = model.dataOriginPackage,
            lastModifiedTime = model.lastModifiedTime,
            clientRecordId = model.clientRecordId,
            deviceManufacturer = model.deviceManufacturer,
            deviceModel = model.deviceModel,
            recordingMethod = model.recordingMethod
        )
    }

    fun getHealthConnectSteps(dto: IHealthConnectStepsDTO): HealthConnectSteps {
        val steps = dto.id?.let { stepsRepository.findById(it) }

        val metadata = metadataRepository.findById(dto.healthConnectMetadataId!!)
            .getOrThrowDefaultException(messageSource, HealthConnectMetadata::class)

        val exerciseExecution = dto.exerciseExecutionId?.let {
            exerciseExecutionRepository.findById(it)
                .getOrThrowDefaultException(messageSource, ExerciseExecution::class)
        }

        return when {
            dto.id == null -> {
                HealthConnectSteps(
                    active = dto.active,
                    healthConnectMetadata = metadata,
                    exerciseExecution = exerciseExecution,
                    count = dto.count,
                    startTime = dto.startTime,
                    endTime = dto.endTime,
                    startZoneOffset = dto.startZoneOffset,
                    endZoneOffset = dto.endZoneOffset
                )
            }
            steps?.isPresent == true -> {
                steps.get().copy(
                    active = dto.active,
                    healthConnectMetadata = metadata,
                    exerciseExecution = exerciseExecution,
                    count = dto.count,
                    startTime = dto.startTime,
                    endTime = dto.endTime,
                    startZoneOffset = dto.startZoneOffset,
                    endZoneOffset = dto.endZoneOffset
                )
            }
            else -> {
                HealthConnectSteps(
                    id = dto.id!!,
                    active = dto.active,
                    healthConnectMetadata = metadata,
                    exerciseExecution = exerciseExecution,
                    count = dto.count,
                    startTime = dto.startTime,
                    endTime = dto.endTime,
                    startZoneOffset = dto.startZoneOffset,
                    endZoneOffset = dto.endZoneOffset
                )
            }
        }
    }

    fun getHealthConnectStepsDTO(model: HealthConnectSteps): ValidatedHealthConnectStepsDTO {
        return ValidatedHealthConnectStepsDTO(
            id = model.id,
            creationDate = model.creationDate,
            updateDate = model.updateDate,
            active = model.active,
            healthConnectMetadataId = model.healthConnectMetadata?.id,
            exerciseExecutionId = model.exerciseExecution?.id,
            count = model.count,
            startTime = model.startTime,
            endTime = model.endTime,
            startZoneOffset = model.startZoneOffset,
            endZoneOffset = model.endZoneOffset
        )
    }

    fun getHealthConnectCaloriesBurned(dto: IHealthConnectCaloriesBurnedDTO): HealthConnectCaloriesBurned {
        val calories = dto.id?.let { caloriesBurnedRepository.findById(it) }

        val metadata = metadataRepository.findById(dto.healthConnectMetadataId!!)
            .getOrThrowDefaultException(messageSource, HealthConnectMetadata::class)

        val exerciseExecution = dto.exerciseExecutionId?.let {
            exerciseExecutionRepository.findById(it)
                .getOrThrowDefaultException(messageSource, ExerciseExecution::class)
        }

        return when {
            dto.id == null -> {
                HealthConnectCaloriesBurned(
                    active = dto.active,
                    healthConnectMetadata = metadata,
                    exerciseExecution = exerciseExecution,
                    caloriesInKcal = dto.caloriesInKcal,
                    startTime = dto.startTime,
                    endTime = dto.endTime,
                    startZoneOffset = dto.startZoneOffset,
                    endZoneOffset = dto.endZoneOffset
                )
            }
            calories?.isPresent == true -> {
                calories.get().copy(
                    active = dto.active,
                    healthConnectMetadata = metadata,
                    exerciseExecution = exerciseExecution,
                    caloriesInKcal = dto.caloriesInKcal,
                    startTime = dto.startTime,
                    endTime = dto.endTime,
                    startZoneOffset = dto.startZoneOffset,
                    endZoneOffset = dto.endZoneOffset
                )
            }
            else -> {
                HealthConnectCaloriesBurned(
                    id = dto.id!!,
                    active = dto.active,
                    healthConnectMetadata = metadata,
                    exerciseExecution = exerciseExecution,
                    caloriesInKcal = dto.caloriesInKcal,
                    startTime = dto.startTime,
                    endTime = dto.endTime,
                    startZoneOffset = dto.startZoneOffset,
                    endZoneOffset = dto.endZoneOffset
                )
            }
        }
    }

    fun getHealthConnectCaloriesBurnedDTO(model: HealthConnectCaloriesBurned): ValidatedHealthConnectCaloriesBurnedDTO {
        return ValidatedHealthConnectCaloriesBurnedDTO(
            id = model.id,
            creationDate = model.creationDate,
            updateDate = model.updateDate,
            active = model.active,
            healthConnectMetadataId = model.healthConnectMetadata?.id,
            exerciseExecutionId = model.exerciseExecution?.id,
            caloriesInKcal = model.caloriesInKcal,
            startTime = model.startTime,
            endTime = model.endTime,
            startZoneOffset = model.startZoneOffset,
            endZoneOffset = model.endZoneOffset
        )
    }
    
    fun getHealthConnectHeartRate(dto: IHealthConnectHeartRateDTO): HealthConnectHeartRate {
        val heartRate = dto.id?.let { heartRateRepository.findById(it) }

        val metadata = metadataRepository.findById(dto.healthConnectMetadataId!!)
            .getOrThrowDefaultException(messageSource, HealthConnectMetadata::class)

        val exerciseExecution = dto.exerciseExecutionId?.let {
            exerciseExecutionRepository.findById(it)
                .getOrThrowDefaultException(messageSource, ExerciseExecution::class)
        }
        
        return when {
            dto.id == null -> {
                HealthConnectHeartRate(
                    active = dto.active,
                    healthConnectMetadata = metadata,
                    exerciseExecution = exerciseExecution,
                    startTime = dto.startTime,
                    endTime = dto.endTime,
                    startZoneOffset = dto.startZoneOffset,
                    endZoneOffset = dto.endZoneOffset
                )
            }
            heartRate?.isPresent == true -> {
                heartRate.get().copy(
                    active = dto.active,
                    healthConnectMetadata = metadata,
                    exerciseExecution = exerciseExecution,
                    startTime = dto.startTime,
                    endTime = dto.endTime,
                    startZoneOffset = dto.startZoneOffset,
                    endZoneOffset = dto.endZoneOffset
                )
            }
            else -> {
                HealthConnectHeartRate(
                    id = dto.id!!,
                    active = dto.active,
                    healthConnectMetadata = metadata,
                    exerciseExecution = exerciseExecution,
                    startTime = dto.startTime,
                    endTime = dto.endTime,
                    startZoneOffset = dto.startZoneOffset,
                    endZoneOffset = dto.endZoneOffset
                )
            }
        }
    }

    fun getHealthConnectHeartRateDTO(model: HealthConnectHeartRate): ValidatedHealthConnectHeartRateDTO {
        return ValidatedHealthConnectHeartRateDTO(
            id = model.id,
            creationDate = model.creationDate,
            updateDate = model.updateDate,
            active = model.active,
            healthConnectMetadataId = model.healthConnectMetadata?.id,
            exerciseExecutionId = model.exerciseExecution?.id,
            startTime = model.startTime,
            endTime = model.endTime,
            startZoneOffset = model.startZoneOffset,
            endZoneOffset = model.endZoneOffset
        )
    }

    fun getHealthConnectHeartRateSamples(dto: IHealthConnectHeartRateSamplesDTO): HealthConnectHeartRateSamples {
        val samples = dto.id?.let { heartRateSamplesRepository.findById(it) }
        
        val heartRateSession = heartRateRepository.findById(dto.healthConnectHeartRateId!!)
            .getOrThrowDefaultException(messageSource, HealthConnectHeartRate::class)

        return when {
            dto.id == null -> {
                HealthConnectHeartRateSamples(
                    active = dto.active,
                    healthConnectHeartRate = heartRateSession,
                    sampleTime = dto.sampleTime,
                    bpm = dto.bpm
                )
            }
            samples?.isPresent == true -> {
                samples.get().copy(
                    active = dto.active,
                    healthConnectHeartRate = heartRateSession,
                    sampleTime = dto.sampleTime,
                    bpm = dto.bpm
                )
            }
            else -> {
                HealthConnectHeartRateSamples(
                    id = dto.id!!,
                    active = dto.active,
                    healthConnectHeartRate = heartRateSession,
                    sampleTime = dto.sampleTime,
                    bpm = dto.bpm
                )
            }
        }
    }
    
    fun getHealthConnectHeartRateSamplesDTO(model: HealthConnectHeartRateSamples): ValidatedHealthConnectHeartRateSamplesDTO {
        return ValidatedHealthConnectHeartRateSamplesDTO(
            id = model.id,
            creationDate = model.creationDate,
            updateDate = model.updateDate,
            active = model.active,
            healthConnectHeartRateId = model.healthConnectHeartRate?.id,
            sampleTime = model.sampleTime,
            bpm = model.bpm
        )
    }

    fun getHealthConnectSleepSession(dto: IHealthConnectSleepSessionDTO): HealthConnectSleepSession {
        val session = dto.id?.let { sleepSessionRepository.findById(it) }

        val metadata = metadataRepository.findById(dto.healthConnectMetadataId!!)
            .getOrThrowDefaultException(messageSource, HealthConnectMetadata::class)

        return when {
            dto.id == null -> {
                HealthConnectSleepSession(
                    active = dto.active,
                    healthConnectMetadata = metadata,
                    startTime = dto.startTime,
                    endTime = dto.endTime,
                    title = dto.title,
                    notes = dto.notes
                )
            }
            session?.isPresent == true -> {
                session.get().copy(
                    active = dto.active,
                    healthConnectMetadata = metadata,
                    startTime = dto.startTime,
                    endTime = dto.endTime,
                    title = dto.title,
                    notes = dto.notes
                )
            }
            else -> {
                HealthConnectSleepSession(
                    id = dto.id!!,
                    active = dto.active,
                    healthConnectMetadata = metadata,
                    startTime = dto.startTime,
                    endTime = dto.endTime,
                    title = dto.title,
                    notes = dto.notes
                )
            }
        }
    }

    fun getHealthConnectSleepSessionDTO(model: HealthConnectSleepSession): ValidatedHealthConnectSleepSessionDTO {
        return ValidatedHealthConnectSleepSessionDTO(
            id = model.id,
            creationDate = model.creationDate,
            updateDate = model.updateDate,
            active = model.active,
            healthConnectMetadataId = model.healthConnectMetadata?.id,
            startTime = model.startTime,
            endTime = model.endTime,
            title = model.title,
            notes = model.notes
        )
    }

    fun getHealthConnectSleepStages(dto: IHealthConnectSleepStagesDTO): HealthConnectSleepStages {
        val stage = dto.id?.let { sleepStagesRepository.findById(it) }
        
        val sleepSession = sleepSessionRepository.findById(dto.healthConnectSleepSessionId!!)
            .getOrThrowDefaultException(messageSource, HealthConnectSleepSession::class)

        return when {
            dto.id == null -> {
                HealthConnectSleepStages(
                    active = dto.active,
                    healthConnectSleepSession = sleepSession,
                    startTime = dto.startTime,
                    endTime = dto.endTime,
                    stage = dto.stage
                )
            }
            stage?.isPresent == true -> {
                stage.get().copy(
                    active = dto.active,
                    healthConnectSleepSession = sleepSession,
                    startTime = dto.startTime,
                    endTime = dto.endTime,
                    stage = dto.stage
                )
            }
            else -> {
                HealthConnectSleepStages(
                    id = dto.id!!,
                    active = dto.active,
                    healthConnectSleepSession = sleepSession,
                    startTime = dto.startTime,
                    endTime = dto.endTime,
                    stage = dto.stage
                )
            }
        }
    }

    fun getHealthConnectSleepStagesDTO(model: HealthConnectSleepStages): ValidatedHealthConnectSleepStagesDTO {
        return ValidatedHealthConnectSleepStagesDTO(
            id = model.id,
            creationDate = model.creationDate,
            updateDate = model.updateDate,
            active = model.active,
            healthConnectSleepSessionId = model.healthConnectSleepSession?.id,
            startTime = model.startTime,
            endTime = model.endTime,
            stage = model.stage
        )
    }

    fun getSleepSessionExerciseExecution(dto: ISleepSessionExerciseExecutionDTO): SleepSessionExerciseExecution {
        val association = dto.id?.let { sleepSessionExerciseExecutionRepository.findById(it) }
        
        val sleepSession = sleepSessionRepository.findById(dto.healthConnectSleepSessionId!!)
            .getOrThrowDefaultException(messageSource, HealthConnectSleepSession::class)
            
        val exerciseExecution = exerciseExecutionRepository.findById(dto.exerciseExecutionId!!)
            .getOrThrowDefaultException(messageSource, ExerciseExecution::class)

        return when {
            dto.id == null -> {
                SleepSessionExerciseExecution(
                    active = dto.active,
                    healthConnectSleepSession = sleepSession,
                    exerciseExecution = exerciseExecution
                )
            }
            association?.isPresent == true -> {
                association.get().copy(
                    active = dto.active,
                    healthConnectSleepSession = sleepSession,
                    exerciseExecution = exerciseExecution
                )
            }
            else -> {
                SleepSessionExerciseExecution(
                    id = dto.id!!,
                    active = dto.active,
                    healthConnectSleepSession = sleepSession,
                    exerciseExecution = exerciseExecution
                )
            }
        }
    }

    fun getSleepSessionExerciseExecutionDTO(model: SleepSessionExerciseExecution): ValidatedSleepSessionExerciseExecutionDTO {
        return ValidatedSleepSessionExerciseExecutionDTO(
            id = model.id,
            creationDate = model.creationDate,
            updateDate = model.updateDate,
            active = model.active,
            healthConnectSleepSessionId = model.healthConnectSleepSession?.id,
            exerciseExecutionId = model.exerciseExecution?.id
        )
    }
}