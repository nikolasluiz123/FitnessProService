package br.com.fitnesspro.shared.communication.dtos.workout.health

import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.health.IHealthConnectStepsDTO
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

data class HealthConnectStepsDTO(
    override var id: String? = null,
    override var creationDate: LocalDateTime? = null,
    override var updateDate: LocalDateTime? = null,
    override var active: Boolean = true,
    override var healthConnectMetadataId: String? = null,
    override var exerciseExecutionId: String? = null,
    override var count: Long? = null,
    override var startTime: Instant? = null,
    override var endTime: Instant? = null,
    override var startZoneOffset: ZoneOffset? = null,
    override var endZoneOffset: ZoneOffset? = null
) : IHealthConnectStepsDTO