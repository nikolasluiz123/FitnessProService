package br.com.fitnesspro.shared.communication.dtos.sync.interfaces

import br.com.fitnesspro.shared.communication.dtos.general.interfaces.IReportDTO
import br.com.fitnesspro.shared.communication.dtos.general.interfaces.IWorkoutReportDTO
import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.*
import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.health.*

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

    var reports: List<IReportDTO>
    var workoutReports: List<IWorkoutReportDTO>

    var metadata: List<IHealthConnectMetadataDTO>
    var steps: List<IHealthConnectStepsDTO>
    var caloriesBurned: List<IHealthConnectCaloriesBurnedDTO>
    var heartRateSessions: List<IHealthConnectHeartRateDTO>
    var heartRateSamples: List<IHealthConnectHeartRateSamplesDTO>
    var sleepSessions: List<IHealthConnectSleepSessionDTO>
    var sleepStages: List<IHealthConnectSleepStagesDTO>
    var sleepSessionAssociations: List<ISleepSessionExerciseExecutionDTO>


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
                videoExercisePreDefinitions.isEmpty() &&

                reports.isEmpty() &&
                workoutReports.isEmpty() &&

                metadata.isEmpty() &&
                steps.isEmpty() &&
                caloriesBurned.isEmpty() &&
                heartRateSessions.isEmpty() &&
                heartRateSamples.isEmpty() &&
                sleepSessions.isEmpty() &&
                sleepStages.isEmpty() &&
                sleepSessionAssociations.isEmpty()
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
            videoExercisePreDefinitions.size,

            reports.size,
            workoutReports.size,


            metadata.size,
            steps.size,
            caloriesBurned.size,
            heartRateSessions.size,
            heartRateSamples.size,
            sleepSessions.size,
            sleepStages.size,
            sleepSessionAssociations.size
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
                videoExercisePreDefinitions.size +

                reports.size +
                workoutReports.size +

                metadata.size +
                steps.size +
                caloriesBurned.size +
                heartRateSessions.size +
                heartRateSamples.size +
                sleepSessions.size +
                sleepStages.size +
                sleepSessionAssociations.size
    }
}