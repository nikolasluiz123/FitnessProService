package br.com.fitnesspro.models.workout

import br.com.fitnesspro.core.extensions.dateTimeNow
import br.com.fitnesspro.models.base.AuditableModel
import br.com.fitnesspro.models.base.IHealthDataRangeEntity
import jakarta.persistence.*
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(
    name = "exercise_execution",
    indexes = [
        Index(name = "idx_exercise_execution_exercise_id", columnList = "exercise_id"),
    ]
)
data class ExerciseExecution(
    @Id
    override var id: String = UUID.randomUUID().toString(),

    @Column(nullable = false)
    override var active: Boolean = true,

    @Column(name = "transmission_date", nullable = false)
    override var transmissionDate: LocalDateTime = dateTimeNow(),

    @Column(name = "creation_date", nullable = false)
    override var creationDate: LocalDateTime = dateTimeNow(),

    @Column(name = "update_date", nullable = false)
    override var updateDate: LocalDateTime = dateTimeNow(),

    var duration: Long? = null,

    var repetitions: Int? = null,

    var set: Int? = null,

    var rest: Long? = null,

    var weight: Double? = null,

    @Column(name = "health_data_collected")
    override var healthDataCollected: Boolean = false,

    @Column(name = "execution_start_time", nullable = false)
    var executionStartTime: Instant = Instant.now(),

    @Column(name = "execution_end_time")
    var executionEndTime: Instant? = null,

    @ManyToOne
    @JoinColumn(name = "exercise_id", nullable = false)
    var exercise: Exercise? = null,
): IHealthDataRangeEntity, AuditableModel {

    @get:Transient
    override val rangeStartTime: Instant
        get() = this.executionStartTime

    @get:Transient
    override val rangeEndTime: Instant?
        get() = this.executionEndTime
}