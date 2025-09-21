package br.com.fitnesspro.service.communication.dtos.sync

import br.com.fitnesspro.shared.communication.dtos.sync.interfaces.IWorkoutModuleSyncDTO
import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.*
import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.health.*
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Classe DTO sincronismo do módulo de treino e Health Connect")
class ValidatedWorkoutModuleSyncDTO(
    override var workouts: List<IWorkoutDTO> = emptyList(),
    override var workoutGroups: List<IWorkoutGroupDTO> = emptyList(),
    override var exercises: List<IExerciseDTO> = emptyList(),
    override var videos: List<IVideoDTO> = emptyList(),
    override var videoExercises: List<IVideoExerciseDTO> = emptyList(),

    override var exerciseExecutions: List<IExerciseExecutionDTO> = emptyList(),
    override var videoExerciseExecutions: List<IVideoExerciseExecutionDTO> = emptyList(),

    override var workoutGroupsPreDefinitions: List<IWorkoutGroupPreDefinitionDTO> = emptyList(),
    override var exercisePredefinitions: List<IExercisePreDefinitionDTO> = emptyList(),
    override var videoExercisePreDefinitions: List<IVideoExercisePreDefinitionDTO> = emptyList(),

    override var metadata: List<IHealthConnectMetadataDTO> = emptyList(),
    override var steps: List<IHealthConnectStepsDTO> = emptyList(),
    override var caloriesBurned: List<IHealthConnectCaloriesBurnedDTO> = emptyList(),
    override var heartRateSessions: List<IHealthConnectHeartRateDTO> = emptyList(),
    override var heartRateSamples: List<IHealthConnectHeartRateSamplesDTO> = emptyList(),
    override var sleepSessions: List<IHealthConnectSleepSessionDTO> = emptyList(),
    override var sleepStages: List<IHealthConnectSleepStagesDTO> = emptyList(),
    override var sleepSessionAssociations: List<ISleepSessionExerciseExecutionDTO> = emptyList()
) : IWorkoutModuleSyncDTO