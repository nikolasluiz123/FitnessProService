package br.com.fitnesspro.shared.communication.dtos.sync

import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.health.*

data class HealthConnectModuleSyncDTO(
    override var metadata: List<IHealthConnectMetadataDTO> = emptyList(),
    override var steps: List<IHealthConnectStepsDTO> = emptyList(),
    override var caloriesBurned: List<IHealthConnectCaloriesBurnedDTO> = emptyList(),
    override var heartRateSessions: List<IHealthConnectHeartRateDTO> = emptyList(),
    override var heartRateSamples: List<IHealthConnectHeartRateSamplesDTO> = emptyList(),
    override var sleepSessions: List<IHealthConnectSleepSessionDTO> = emptyList(),
    override var sleepStages: List<IHealthConnectSleepStagesDTO> = emptyList(),
    override var sleepSessionAssociations: List<ISleepSessionExerciseExecutionDTO> = emptyList()
) : IHealthConnectModuleSyncDTO