package br.com.fitnesspro.models.scheduledtask

import br.com.fitnesspro.core.extensions.dateTimeNow
import br.com.fitnesspro.models.base.AuditableModel
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "scheduled_task")
data class ScheduledTask(
    @Id
    override var id: String = UUID.randomUUID().toString(),

    @Column(nullable = false)
    override var active: Boolean = true,

    @Column(name = "creation_date", nullable = false)
    override var creationDate: LocalDateTime = dateTimeNow(),

    @Column(name = "update_date", nullable = false)
    override var updateDate: LocalDateTime = dateTimeNow(),

    @Column(nullable = false)
    var name: String? = null,

    @Column(name = "interval_millis", nullable = false)
    var intervalMillis: Long? = null,

    @Column(name = "last_execution_time")
    var lastExecutionTime: LocalDateTime? = null,

    @Column(name = "handler_bean_name", nullable = false)
    var handlerBeanName: String? = null,

    @Column(name = "config_json", columnDefinition = "TEXT", nullable = false)
    var configJson: String? = null,
): AuditableModel