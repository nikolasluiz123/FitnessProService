package br.com.fitnesspro.shared.communication.dtos.workout.health

import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.health.ISleepSessionExerciseExecutionDTO
import java.time.LocalDateTime

data class SleepSessionExerciseExecutionDTO(
    override var id: String? = null,
    override var creationDate: LocalDateTime? = null,
    override var updateDate: LocalDateTime? = null,
    override var active: Boolean = true,
    override var healthConnectSleepSessionId: String? = null,
    override var exerciseExecutionId: String? = null
) : ISleepSessionExerciseExecutionDTO