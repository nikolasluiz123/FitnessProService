package br.com.fitnesspro.service.models.logs

import br.com.fitnesspro.core.extensions.dateTimeNow
import br.com.fitnesspro.shared.communication.enums.execution.EnumExecutionType
import br.com.fitnesspro.service.models.general.User
import br.com.fitnesspro.service.models.serviceauth.Application
import br.com.fitnesspro.service.models.serviceauth.Device
import br.com.fitnesspro.shared.communication.enums.execution.EnumExecutionState
import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Table(
    name = "execution_log",
    indexes = [
        Index(name = "idx_execution_log_type", columnList = "type"),
        Index(name = "idx_execution_log_state", columnList = "state"),
        Index(name = "idx_execution_log_user_id", columnList = "user_id"),
        Index(name = "idx_execution_log_device_id", columnList = "device_id"),
    ]
)
@Entity
data class ExecutionLog(
    @Id
    val id: String = UUID.randomUUID().toString(),

    @Column(nullable = false)
    var type: EnumExecutionType? = null,

    @Column(nullable = false)
    var state: EnumExecutionState = EnumExecutionState.PENDING,

    @Column(name = "end_point", nullable = false)
    var endPoint: String? = null,

    @Column(name = "method_name")
    var methodName: String? = null,

    @Column(name = "page_size")
    var pageSize: Int? = null,

    @Column(name = "last_update_date")
    var lastUpdateDate: LocalDateTime? = null,

    @JoinColumn(name = "user_id", nullable = true)
    @ManyToOne
    var user: User? = null,

    @JoinColumn(name = "device_id", nullable = true)
    @ManyToOne
    var device: Device? = null,

    @JoinColumn(name = "application_id", nullable = true)
    @ManyToOne
    var application: Application? = null,

    @Column(name = "creation_date", nullable = false)
    var creationDate: LocalDateTime = dateTimeNow()
)