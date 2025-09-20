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
    name = "health_connect_heart_rate_samples",
    indexes = [
        Index(name = "idx_health_connect_heart_rate_samples_hr_id", columnList = "health_connect_heart_rate_id")
    ]
)
data class HealthConnectHeartRateSamples(
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
    @JoinColumn(name = "health_connect_heart_rate_id", nullable = false)
    var healthConnectHeartRate: HealthConnectHeartRate? = null,

    @Column(name = "sample_time")
    var sampleTime: Instant? = null,

    @Column
    var bpm: Long? = null
) : IntegratedModel, AuditableModel