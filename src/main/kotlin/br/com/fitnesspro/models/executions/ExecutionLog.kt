package br.com.fitnesspro.models.executions

import br.com.fitnesspro.models.executions.enums.EnumExecutionState
import br.com.fitnesspro.models.executions.enums.EnumExecutionType
import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Table(
    name = "execution_log",
    indexes = [
        Index(name = "idx_execution_log_type", columnList = "type"),
        Index(name = "idx_execution_log_state", columnList = "state"),
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

    @Column(name = "execution_start", nullable = false)
    var executionStart: LocalDateTime? = null,

    @Column(name = "execution_end")
    var executionEnd: LocalDateTime? = null,

    @Column(name = "end_point", nullable = false)
    var endPoint: String? = null,

    @Column(name = "result_log", columnDefinition = "TEXT")
    var resultLog: String? = null
)