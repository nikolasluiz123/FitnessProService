package br.com.fitnesspro.service.models.general

import br.com.fitnesspro.service.extensions.dateTimeNow
import br.com.fitnesspro.service.models.base.AuditableModel
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
        Index(name = "idx_person_academy_time_academy_id", columnList = "academy_id")
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

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "person_id", nullable = false)
    var person: Person? = null,

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "academy_id", nullable = false)
    var academy: Academy? = null,

    @Column(name = "time_start", nullable = false)
    var timeStart: LocalTime? = null,

    @Column(name = "time_end", nullable = false)
    var timeEnd: LocalTime? = null,

    @Column(name = "day_week", nullable = false)
    var dayOfWeek: DayOfWeek? = null,
): AuditableModel()