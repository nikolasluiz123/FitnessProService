package br.com.fitnesspro.models.workout

import br.com.fitnesspro.core.extensions.dateTimeNow
import br.com.fitnesspro.models.base.AuditableModel
import br.com.fitnesspro.models.base.IntegratedModel
import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(
    name = "video_exercise_execution",
    indexes = [
        Index(name = "idx_video_exercise_execution_exercise_execution_id", columnList = "exercise_execution_id"),
        Index(name = "idx_video_exercise_execution_video_id", columnList = "video_id"),
    ]
)
data class VideoExerciseExecution(
    @Id
    override val id: String = UUID.randomUUID().toString(),

    @Column(name = "transmission_date", nullable = false)
    override var transmissionDate: LocalDateTime = dateTimeNow(),

    @Column(name = "creation_date", nullable = false)
    override var creationDate: LocalDateTime = dateTimeNow(),

    @Column(name = "update_date", nullable = false)
    override var updateDate: LocalDateTime = dateTimeNow(),

    @Column(nullable = false)
    override var active: Boolean = true,

    @JoinColumn(name = "exercise_execution_id", nullable = false)
    @ManyToOne
    var exerciseExecution: ExerciseExecution? = null,

    @JoinColumn(name = "video_id", nullable = false)
    @ManyToOne
    var video: Video? = null
): IntegratedModel, AuditableModel