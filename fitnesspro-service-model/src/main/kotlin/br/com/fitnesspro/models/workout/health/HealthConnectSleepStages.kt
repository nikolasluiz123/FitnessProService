package br.com.fitnesspro.models.workout.health

import br.com.fitnesspro.core.extensions.dateTimeNow
import br.com.fitnesspro.models.base.AuditableModel
import br.com.fitnesspro.models.base.IntegratedModel
import br.com.fitnesspro.shared.communication.enums.health.EnumSleepStage
import jakarta.persistence.*
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(
    name = "health_connect_sleep_stages",
    indexes = [
        Index(name = "idx_health_connect_sleep_stages_session_id", columnList = "health_connect_sleep_session_id")
    ]
)
data class HealthConnectSleepStages(
    @Id
    override val id: String = UUID.randomUUID().toString(),

    @Column(nullable = false)
    override var active: Boolean = true,

    @Column(name = "creation_date", nullable = false)
    override var creationDate: LocalDateTime = dateTimeNow(),

    @Column(name = "update_date", nullable = false)
    override var updateDate: LocalDateTime = dateTimeNow(),

    @Column(name = "transmission_date", nullable = false)
    override var transmissionDate: LocalDateTime = dateTimeNow(),

    @ManyToOne(optional = false)
    @JoinColumn(name = "health_connect_sleep_session_id", nullable = false)
    var healthConnectSleepSession: HealthConnectSleepSession? = null,

    @Column(name = "start_time")
    var startTime: Instant? = null,

    @Column(name = "end_time")
    var endTime: Instant? = null,

    @Column
    var stage: EnumSleepStage? = null
) : IntegratedModel, AuditableModel