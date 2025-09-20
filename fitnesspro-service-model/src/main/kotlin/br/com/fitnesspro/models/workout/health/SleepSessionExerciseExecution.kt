package br.com.fitnesspro.models.workout.health

import br.com.fitnesspro.core.extensions.dateTimeNow
import br.com.fitnesspro.models.base.AuditableModel
import br.com.fitnesspro.models.base.IntegratedModel
import br.com.fitnesspro.models.workout.ExerciseExecution
import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(
    name = "sleep_session_exercise_execution",
    indexes = [
        Index(name = "idx_sleep_session_exec_exec_session_id", columnList = "health_connect_sleep_session_id"),
        Index(name = "idx_sleep_session_exec_exec_exec_id", columnList = "exercise_execution_id")
    ]
)
data class SleepSessionExerciseExecution(
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

    @ManyToOne(optional = false)
    @JoinColumn(name = "exercise_execution_id", nullable = false)
    var exerciseExecution: ExerciseExecution? = null

) : IntegratedModel, AuditableModel