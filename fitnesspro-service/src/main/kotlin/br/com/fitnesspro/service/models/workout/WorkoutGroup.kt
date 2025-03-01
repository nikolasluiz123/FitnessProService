package br.com.fitnesspro.service.models.workout

import br.com.fitnesspro.core.extensions.dateTimeNow
import br.com.fitnesspro.service.models.base.AuditableModel
import br.com.fitnesspro.service.models.base.IntegratedModel
import br.com.fitnesspro.service.models.general.User
import jakarta.persistence.*
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(
    name = "workout_group",
    indexes = [
        Index(name = "idx_workout_group_workout_id", columnList = "workout_id"),
        Index(name = "idx_workout_group_creation_user_id", columnList = "creation_user_id"),
        Index(name = "idx_workout_group_update_user_id", columnList = "update_user_id")
    ]
)
data class WorkoutGroup(
    @Id
    override val id: String? = UUID.randomUUID().toString(),

    override var active: Boolean = true,

    @Column(name = "creation_date", nullable = false)
    override var creationDate: LocalDateTime = dateTimeNow(),

    @Column(name = "update_date", nullable = false)
    override var updateDate: LocalDateTime = dateTimeNow(),

    @Column(name = "transmission_date", nullable = false)
    override var transmissionDate: LocalDateTime = dateTimeNow(),

    @ManyToOne
    @JoinColumn(name = "creation_user_id", nullable = false)
    override var creationUser: User? = null,

    @ManyToOne
    @JoinColumn(name = "update_user_id", nullable = false)
    override var updateUser: User? = null,

    @Column(nullable = false)
    var name: String? = null,

    @JoinColumn(name = "workout_id", nullable = false)
    @ManyToOne
    var workout: Workout? = null,

    @Column(name = "day_week", nullable = false)
    var dayWeek: DayOfWeek? = null,
) : IntegratedModel, AuditableModel