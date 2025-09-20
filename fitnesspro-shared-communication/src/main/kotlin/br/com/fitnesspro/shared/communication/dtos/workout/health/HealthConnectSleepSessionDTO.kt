package br.com.fitnesspro.shared.communication.dtos.workout.health

import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.health.IHealthConnectSleepSessionDTO
import java.time.Instant
import java.time.LocalDateTime

data class HealthConnectSleepSessionDTO(
    override var id: String? = null,
    override var creationDate: LocalDateTime? = null,
    override var updateDate: LocalDateTime? = null,
    override var active: Boolean = true,
    override var healthConnectMetadataId: String? = null,
    override var startTime: Instant? = null,
    override var endTime: Instant? = null,
    override var title: String? = null,
    override var notes: String? = null
) : IHealthConnectSleepSessionDTO