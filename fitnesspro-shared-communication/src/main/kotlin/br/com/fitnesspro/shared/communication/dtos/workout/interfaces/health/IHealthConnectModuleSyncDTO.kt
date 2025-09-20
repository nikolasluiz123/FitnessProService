package br.com.fitnesspro.shared.communication.dtos.workout.interfaces.health

import br.com.fitnesspro.shared.communication.dtos.sync.interfaces.ISyncDTO

interface IHealthConnectModuleSyncDTO : ISyncDTO {

    var metadata: List<IHealthConnectMetadataDTO>
    var steps: List<IHealthConnectStepsDTO>
    var caloriesBurned: List<IHealthConnectCaloriesBurnedDTO>
    var heartRateSessions: List<IHealthConnectHeartRateDTO>
    var heartRateSamples: List<IHealthConnectHeartRateSamplesDTO>
    var sleepSessions: List<IHealthConnectSleepSessionDTO>
    var sleepStages: List<IHealthConnectSleepStagesDTO>
    var sleepSessionAssociations: List<ISleepSessionExerciseExecutionDTO>

    override fun isEmpty(): Boolean {
        return metadata.isEmpty() &&
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
        return metadata.size +
                steps.size +
                caloriesBurned.size +
                heartRateSessions.size +
                heartRateSamples.size +
                sleepSessions.size +
                sleepStages.size +
                sleepSessionAssociations.size
    }
}