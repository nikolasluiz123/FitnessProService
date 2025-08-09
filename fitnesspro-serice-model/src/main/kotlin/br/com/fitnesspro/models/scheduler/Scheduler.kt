package br.com.fitnesspro.models.scheduler

import br.com.fitnesspro.core.extensions.dateTimeNow
import br.com.fitnesspro.models.base.AuditableModel
import br.com.fitnesspro.models.base.IntegratedModel
import br.com.fitnesspro.models.general.Person
import br.com.fitnesspro.shared.communication.enums.scheduler.EnumCompromiseType
import br.com.fitnesspro.shared.communication.enums.scheduler.EnumSchedulerSituation
import jakarta.persistence.*
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.util.*

@Entity
@Table(
    name = "scheduler",
    indexes = [
        Index(name = "idx_scheduler_academy_member_person_id", columnList = "academy_member_person_id"),
        Index(name = "idx_scheduler_professional_person_id", columnList = "professional_person_id"),
        Index(name = "idx_scheduler_cancellation_person_id", columnList = "cancellation_person_id"),
    ]
)
data class Scheduler(
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
    @JoinColumn(name = "academy_member_person_id", nullable = false)
    var academyMemberPerson: Person? = null,

    @ManyToOne
    @JoinColumn(name = "professional_person_id", nullable = false)
    var professionalPerson: Person? = null,

    @Column(name = "date_time_start", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    var dateTimeStart: OffsetDateTime? = null,

    @Column(name = "date_time_end", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    var dateTimeEnd: OffsetDateTime? = null,

    @Column(name = "canceled_date", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    var canceledDate: OffsetDateTime? = null,

    @ManyToOne
    @JoinColumn(name = "cancellation_person_id")
    var cancellationPerson: Person? = null,

    @Column(nullable = false)
    var situation: EnumSchedulerSituation? = null,

    @Column(name = "compromise_type", nullable = false)
    var compromiseType: EnumCompromiseType? = null,

    @Column(length = 4096)
    var observation: String? = null,

    @Column(name = "notified_antecedence", columnDefinition = "boolean default false   ")
    var notifiedAntecedence: Boolean = false,
): IntegratedModel, AuditableModel