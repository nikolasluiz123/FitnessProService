package br.com.fitnesspro.models.workout.health

import br.com.fitnesspro.core.extensions.dateTimeNow
import br.com.fitnesspro.models.base.AuditableModel
import br.com.fitnesspro.models.base.IntegratedModel
import jakarta.persistence.*
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(
    name = "health_connect_sleep_session",
    indexes = [
        Index(name = "idx_health_connect_sleep_session_metadata_id", columnList = "health_connect_metadata_id"),
    ]
)
data class HealthConnectSleepSession(
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
    @JoinColumn(name = "health_connect_metadata_id", nullable = false)
    var healthConnectMetadata: HealthConnectMetadata? = null,

    @Column(name = "start_time")
    var startTime: Instant? = null,

    @Column(name = "end_time")
    var endTime: Instant? = null,

    @Column
    var title: String? = null,

    @Column(columnDefinition = "TEXT")
    var notes: String? = null,
) : IntegratedModel, AuditableModel