package br.com.fitnesspro.models.workout

import br.com.fitnesspro.core.extensions.dateTimeNow
import br.com.fitnesspro.models.base.AuditableModel
import br.com.fitnesspro.models.base.IntegratedModel
import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(
    name = "exercise",
    indexes = [
        Index(name = "idx_exercise_workout_group_id", columnList = "workout_group_id"),
    ]
)
data class Exercise(
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

    @Column(nullable = false)
    var name: String? = null,

    var duration: Long? = null,

    var repetitions: Int? = null,

    var sets: Int? = null,

    var rest: Long? = null,

    @Column(length = 4096)
    var observation: String? = null,

    @JoinColumn(name = "workout_group_id", nullable = false)
    @ManyToOne
    var workoutGroup: WorkoutGroup? = null,

    @Column(name = "exercise_order", nullable = false)
    var exerciseOrder: Int? = null,
): IntegratedModel, AuditableModel