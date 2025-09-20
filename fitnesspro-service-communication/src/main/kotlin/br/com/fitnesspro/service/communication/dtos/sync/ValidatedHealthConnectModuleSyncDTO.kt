package br.com.fitnesspro.service.communication.dtos.sync

import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.health.*
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Classe DTO para sincronismo do módulo Health Connect")
data class ValidatedHealthConnectModuleSyncDTO(
    override var metadata: List<IHealthConnectMetadataDTO> = emptyList(),
    override var steps: List<IHealthConnectStepsDTO> = emptyList(),
    override var caloriesBurned: List<IHealthConnectCaloriesBurnedDTO> = emptyList(),
    override var heartRateSessions: List<IHealthConnectHeartRateDTO> = emptyList(),
    override var heartRateSamples: List<IHealthConnectHeartRateSamplesDTO> = emptyList(),
    override var sleepSessions: List<IHealthConnectSleepSessionDTO> = emptyList(),
    override var sleepStages: List<IHealthConnectSleepStagesDTO> = emptyList(),
    override var sleepSessionAssociations: List<ISleepSessionExerciseExecutionDTO> = emptyList()
) : IHealthConnectModuleSyncDTO