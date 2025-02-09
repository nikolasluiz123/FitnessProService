package br.com.fitnesspro.models.scheduler

import br.com.fitnesspro.models.base.BaseModel
import br.com.fitnesspro.models.general.Person
import br.com.fitnesspro.models.scheduler.enums.EnumCompromiseType
import br.com.fitnesspro.models.scheduler.enums.EnumSchedulerSituation
import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

@Entity
@Table(
    name = "scheduler",
    indexes = [
        Index(name = "idx_scheduler_academy_member_person_id", columnList = "academy_member_person_id"),
        Index(name = "idx_scheduler_professional_person_id", columnList = "professional_person_id")
    ]
)
data class Scheduler(
    @Id
    override val id: String = UUID.randomUUID().toString(),
    override var active: Boolean = true,

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "academy_member_person_id", nullable = false)
    var academyMemberPerson: Person? = null,

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "professional_person_id", nullable = false)
    var professionalPerson: Person? = null,

    @Column(name = "scheduled_date", nullable = false)
    var scheduledDate: LocalDate? = null,

    @Column(name = "time_start", nullable = false)
    var timeStart: LocalTime? = null,

    @Column(name = "time_end", nullable = false)
    var timeEnd: LocalTime? = null,

    @Column(name = "canceled_date")
    var canceledDate: LocalDateTime? = null,

    @Column(nullable = false)
    var situation: EnumSchedulerSituation? = null,

    @Column(name = "compromise_type", nullable = false)
    var compromiseType: EnumCompromiseType? = null,

    @Column(length = 4096)
    var observation: String? = null
): BaseModel()