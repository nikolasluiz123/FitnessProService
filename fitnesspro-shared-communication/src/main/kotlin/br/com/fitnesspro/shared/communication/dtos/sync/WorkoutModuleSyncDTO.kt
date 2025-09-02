package br.com.fitnesspro.shared.communication.dtos.sync

import br.com.fitnesspro.shared.communication.dtos.sync.interfaces.IWorkoutModuleSyncDTO
import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.*

class WorkoutModuleSyncDTO(
    override var workouts: List<IWorkoutDTO> = emptyList(),
    override var workoutGroups: List<IWorkoutGroupDTO> = emptyList(),
    override var exercises: List<IExerciseDTO> = emptyList(),
    override var videos: List<IVideoDTO> = emptyList(),
    override var videoExercises: List<IVideoExerciseDTO> = emptyList(),

    override var exerciseExecutions: List<IExerciseExecutionDTO> = emptyList(),
    override var videoExerciseExecutions: List<IVideoExerciseExecutionDTO> = emptyList(),

    override var workoutGroupsPreDefinitions: List<IWorkoutGroupPreDefinitionDTO> = emptyList(),
    override var exercisePredefinitions: List<IExercisePreDefinitionDTO> = emptyList(),
    override var videoExercisePreDefinitions: List<IVideoExercisePreDefinitionDTO> = emptyList()
) : IWorkoutModuleSyncDTO {
}