package br.com.fitnesspro.models.workout

import br.com.fitnesspro.core.extensions.dateTimeNow
import br.com.fitnesspro.models.base.AuditableModel
import br.com.fitnesspro.models.base.IntegratedModel
import br.com.fitnesspro.models.general.Person
import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(
    name = "exercise_pre_definition",
    indexes = [
        Index(name = "idx_exercise_pre_definition_personal_trainer_person_id", columnList = "personal_trainer_person_id"),
        Index(name = "idx_exercise_pre_definition_workout_group_pre_definition_id", columnList = "workout_group_pre_definition_id"),
    ]
)
data class ExercisePreDefinition(
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

    @Column(name = "excercise_order")
    var exerciseOrder: Int? = null,

    @JoinColumn(name = "personal_trainer_person_id")
    @ManyToOne(optional = false)
    var personalTrainerPerson: Person? = null,

    @JoinColumn(name = "workout_group_pre_definition_id")
    @ManyToOne
    var workoutGroupPreDefinition: WorkoutGroupPreDefinition? = null,
): IntegratedModel, AuditableModel