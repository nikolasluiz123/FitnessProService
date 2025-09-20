package br.com.fitnesspro.shared.communication.dtos.workout.health

import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.health.IHealthConnectSleepStagesDTO
import br.com.fitnesspro.shared.communication.enums.health.EnumSleepStage
import java.time.Instant
import java.time.LocalDateTime

data class HealthConnectSleepStagesDTO(
    override var id: String? = null,
    override var creationDate: LocalDateTime? = null,
    override var updateDate: LocalDateTime? = null,
    override var active: Boolean = true,
    override var healthConnectSleepSessionId: String? = null,
    override var startTime: Instant? = null,
    override var endTime: Instant? = null,
    override var stage: EnumSleepStage? = null
) : IHealthConnectSleepStagesDTO