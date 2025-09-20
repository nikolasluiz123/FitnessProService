package br.com.fitnesspro.shared.communication.dtos.workout.health

import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.health.IHealthConnectHeartRateSamplesDTO
import java.time.Instant
import java.time.LocalDateTime

data class HealthConnectHeartRateSamplesDTO(
    override var id: String? = null,
    override var creationDate: LocalDateTime? = null,
    override var updateDate: LocalDateTime? = null,
    override var active: Boolean = true,
    override var healthConnectHeartRateId: String? = null,
    override var sampleTime: Instant? = null,
    override var bpm: Long? = null
) : IHealthConnectHeartRateSamplesDTO