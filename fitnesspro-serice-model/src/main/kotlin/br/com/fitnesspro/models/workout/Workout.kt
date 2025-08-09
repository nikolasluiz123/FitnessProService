package br.com.fitnesspro.models.workout

import br.com.fitnesspro.core.extensions.dateTimeNow
import br.com.fitnesspro.models.base.AuditableModel
import br.com.fitnesspro.models.base.IntegratedModel
import br.com.fitnesspro.models.general.Person
import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(
    name = "workout",
    indexes = [
        Index(name = "idx_workout_academy_member_person_id", columnList = "academy_member_person_id"),
        Index(name = "idx_workout_professional_person_id", columnList = "professional_person_id"),
    ]
)
data class Workout(
    @Id
    override val id: String? = UUID.randomUUID().toString(),

    override var active: Boolean = true,

    @Column(name = "creation_date", nullable = false)
    override var creationDate: LocalDateTime = dateTimeNow(),

    @Column(name = "update_date", nullable = false)
    override var updateDate: LocalDateTime = dateTimeNow(),

    @Column(name = "transmission_date", nullable = false)
    override var transmissionDate: LocalDateTime = dateTimeNow(),

    @JoinColumn(name = "academy_member_person_id", nullable = false)
    @ManyToOne
    var academyMemberPerson: Person? = null,

    @JoinColumn(name = "professional_person_id", nullable = false)
    @ManyToOne
    var professionalPerson: Person? = null,

    @Column(name = "date_start", nullable = false)
    var dateStart: LocalDate? = null,

    @Column(name = "date_end", nullable = false)
    var dateEnd: LocalDate? = null,
) : IntegratedModel, AuditableModel