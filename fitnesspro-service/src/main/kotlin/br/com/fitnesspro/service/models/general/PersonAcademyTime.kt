package br.com.fitnesspro.service.models.general

import br.com.fitnesspro.core.extensions.dateTimeNow
import br.com.fitnesspro.service.models.base.IntegratedModel
import jakarta.persistence.*
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

@Entity
@Table(
    name = "person_academy_time",
    indexes = [
        Index(name = "idx_person_academy_time_person_id", columnList = "person_id"),
        Index(name = "idx_person_academy_time_academy_id", columnList = "academy_id"),
        Index(name = "idx_person_academy_time_creation_user_id", columnList = "creation_user_id"),
        Index(name = "idx_person_academy_time_update_user_id", columnList = "update_user_id")
    ]
)
data class PersonAcademyTime(
    @Id
    override val id: String = UUID.randomUUID().toString(),

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

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    var person: Person? = null,

    @ManyToOne
    @JoinColumn(name = "academy_id", nullable = false)
    var academy: Academy? = null,

    @Column(name = "time_start", nullable = false)
    var timeStart: LocalTime? = null,

    @Column(name = "time_end", nullable = false)
    var timeEnd: LocalTime? = null,

    @Column(name = "day_week", nullable = false)
    var dayOfWeek: DayOfWeek? = null,
): IntegratedModel()