package br.com.fitnesspro.service.communication.dtos.sync

import br.com.fitnesspro.service.communication.dtos.annotation.EntityReference
import br.com.fitnesspro.shared.communication.dtos.sync.interfaces.IWorkoutModuleSyncDTO
import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.*
import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.health.*
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Classe DTO sincronismo do módulo de treino e Health Connect")
class ValidatedWorkoutModuleSyncDTO(
    @EntityReference("Workout")
    override var workouts: List<IWorkoutDTO> = emptyList(),

    @EntityReference("WorkoutGroup")
    override var workoutGroups: List<IWorkoutGroupDTO> = emptyList(),

    @EntityReference("Exercise")
    override var exercises: List<IExerciseDTO> = emptyList(),

    @EntityReference("Video")
    override var videos: List<IVideoDTO> = emptyList(),

    @EntityReference("VideoExercise")
    override var videoExercises: List<IVideoExerciseDTO> = emptyList(),

    @EntityReference("ExerciseExecution")
    override var exerciseExecutions: List<IExerciseExecutionDTO> = emptyList(),

    @EntityReference("VideoExerciseExecution")
    override var videoExerciseExecutions: List<IVideoExerciseExecutionDTO> = emptyList(),

    @EntityReference("WorkoutGroupPreDefinition")
    override var workoutGroupsPreDefinitions: List<IWorkoutGroupPreDefinitionDTO> = emptyList(),

    @EntityReference("ExercisePreDefinition")
    override var exercisePredefinitions: List<IExercisePreDefinitionDTO> = emptyList(),

    @EntityReference("VideoExercisePreDefinition")
    override var videoExercisePreDefinitions: List<IVideoExercisePreDefinitionDTO> = emptyList(),

    @EntityReference("HealthConnectMetadata")
    override var metadata: List<IHealthConnectMetadataDTO> = emptyList(),

    @EntityReference("HealthConnectSteps")
    override var steps: List<IHealthConnectStepsDTO> = emptyList(),

    @EntityReference("HealthConnectCaloriesBurned")
    override var caloriesBurned: List<IHealthConnectCaloriesBurnedDTO> = emptyList(),

    @EntityReference("HealthConnectHeartRate")
    override var heartRateSessions: List<IHealthConnectHeartRateDTO> = emptyList(),

    @EntityReference("HealthConnectHeartRateSamples")
    override var heartRateSamples: List<IHealthConnectHeartRateSamplesDTO> = emptyList(),

    @EntityReference("HealthConnectSleepSession")
    override var sleepSessions: List<IHealthConnectSleepSessionDTO> = emptyList(),

    @EntityReference("HealthConnectSleepStages")
    override var sleepStages: List<IHealthConnectSleepStagesDTO> = emptyList(),

    @EntityReference("SleepSessionExerciseExecution")
    override var sleepSessionAssociations: List<ISleepSessionExerciseExecutionDTO> = emptyList()
) : IWorkoutModuleSyncDTO