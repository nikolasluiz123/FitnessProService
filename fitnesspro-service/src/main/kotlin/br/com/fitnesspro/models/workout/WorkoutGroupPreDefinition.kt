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
    name = "workout_group_pre_definition",
    indexes = [
        Index(name = "idx_workout_group_pre_definition_personal_trainer_person_id", columnList = "personal_trainer_person_id"),
    ]
)
data class WorkoutGroupPreDefinition(
    @Id
    override val id: String? = UUID.randomUUID().toString(),

    override var active: Boolean = true,

    @Column(name = "creation_date", nullable = false)
    override var creationDate: LocalDateTime = dateTimeNow(),

    @Column(name = "update_date", nullable = false)
    override var updateDate: LocalDateTime = dateTimeNow(),

    @Column(name = "transmission_date", nullable = false)
    override var transmissionDate: LocalDateTime = dateTimeNow(),

    @Column(nullable = false)
    var name: String? = null,

    @JoinColumn(name = "personal_trainer_person_id")
    @ManyToOne(optional = false)
    var personalTrainerPerson: Person? = null,
) : IntegratedModel, AuditableModel