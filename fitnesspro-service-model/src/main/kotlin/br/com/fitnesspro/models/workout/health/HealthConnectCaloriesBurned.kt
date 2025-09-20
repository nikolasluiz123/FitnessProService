package br.com.fitnesspro.models.workout.health

import br.com.fitnesspro.core.extensions.dateTimeNow
import br.com.fitnesspro.models.base.AuditableModel
import br.com.fitnesspro.models.base.IntegratedModel
import br.com.fitnesspro.models.workout.ExerciseExecution
import jakarta.persistence.*
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

@Entity
@Table(
    name = "health_connect_calories_burned",
    indexes = [
        Index(name = "idx_health_connect_calories_burned_metadata_id", columnList = "health_connect_metadata_id"),
        Index(name = "idx_health_connect_calories_burned_exec_exec_id", columnList = "exercise_execution_id")
    ]
)
data class HealthConnectCaloriesBurned(
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

    @ManyToOne(optional = false)
    @JoinColumn(name = "exercise_execution_id")
    var exerciseExecution: ExerciseExecution? = null,

    @Column(name = "calories_in_kcal")
    var caloriesInKcal: Long? = null,

    @Column(name = "start_time")
    var startTime: Instant? = null,

    @Column(name = "end_time")
    var endTime: Instant? = null,

    @Column(name = "start_zone_offset")
    var startZoneOffset: ZoneOffset? = null,

    @Column(name = "end_zone_offset")
    var endZoneOffset: ZoneOffset? = null,
) : IntegratedModel, AuditableModel