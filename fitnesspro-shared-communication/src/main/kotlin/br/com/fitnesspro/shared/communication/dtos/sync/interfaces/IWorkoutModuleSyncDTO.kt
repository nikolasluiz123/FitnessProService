package br.com.fitnesspro.shared.communication.dtos.sync.interfaces

import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.*

interface IWorkoutModuleSyncDTO : ISyncDTO {
    var workouts: List<IWorkoutDTO>
    var workoutGroups: List<IWorkoutGroupDTO>
    var exercises: List<IExerciseDTO>
    var videos: List<IVideoDTO>
    var videoExercises: List<IVideoExerciseDTO>

    var exerciseExecutions: List<IExerciseExecutionDTO>
    var videoExerciseExecutions: List<IVideoExerciseExecutionDTO>

    var workoutGroupsPreDefinitions: List<IWorkoutGroupPreDefinitionDTO>
    var exercisePredefinitions: List<IExercisePreDefinitionDTO>
    var videoExercisePreDefinitions: List<IVideoExercisePreDefinitionDTO>

    override fun isEmpty(): Boolean {
        return workouts.isEmpty() &&
                workoutGroups.isEmpty() &&
                exercises.isEmpty() &&
                videos.isEmpty() &&
                videoExercises.isEmpty() &&

                exerciseExecutions.isEmpty() &&
                videoExerciseExecutions.isEmpty() &&

                workoutGroupsPreDefinitions.isEmpty() &&
                exercisePredefinitions.isEmpty() &&
                videoExercisePreDefinitions.isEmpty()
    }

    override fun getMaxListSize(): Int {
        return maxOf(
            workouts.size,
            workoutGroups.size,
            exercises.size,
            videos.size,
            videoExercises.size,

            exerciseExecutions.size,
            videoExerciseExecutions.size,

            workoutGroupsPreDefinitions.size,
            exercisePredefinitions.size,
            videoExercisePreDefinitions.size
        )
    }

    override fun getItemsCount(): Int {
        return workouts.size +
                workoutGroups.size +
                exercises.size +
                videos.size +
                videoExercises.size +

                exerciseExecutions.size +
                videoExerciseExecutions.size +

                workoutGroupsPreDefinitions.size +
                exercisePredefinitions.size +
                videoExercisePreDefinitions.size
    }
}