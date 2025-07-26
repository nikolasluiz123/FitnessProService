package br.com.fitnesspro.models.general

import br.com.fitnesspro.core.extensions.dateTimeNow
import br.com.fitnesspro.models.base.AuditableModel
import br.com.fitnesspro.models.base.IntegratedModel
import br.com.fitnesspro.shared.communication.enums.report.EnumReportContext
import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "scheduler_report")
data class SchedulerReport(
    @Id
    override val id: String = UUID.randomUUID().toString(),

    override var active: Boolean = true,

    @Column(name = "creation_date", nullable = false)
    override var creationDate: LocalDateTime = dateTimeNow(),

    @Column(name = "update_date", nullable = false)
    override var updateDate: LocalDateTime = dateTimeNow(),

    @Column(name = "transmission_date", nullable = false)
    override var transmissionDate: LocalDateTime = dateTimeNow(),

    @JoinColumn(name = "person_id", nullable = false)
    @ManyToOne(optional = false)
    var person: Person? = null,

    @JoinColumn(name = "report_id", nullable = false, unique = true)
    @OneToOne(optional = false)
    var report: Report? = null,

    @Column(name = "report_context", nullable = false)
    var reportContext: EnumReportContext? = null
) : IntegratedModel, AuditableModel