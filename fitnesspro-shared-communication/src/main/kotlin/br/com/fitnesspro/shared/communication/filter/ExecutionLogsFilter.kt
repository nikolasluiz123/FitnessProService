package br.com.fitnesspro.shared.communication.filter

import br.com.fitnesspro.models.executions.enums.EnumExecutionType
import br.com.fitnesspro.shared.communication.enums.execution.EnumExecutionState
import java.time.LocalDateTime

data class ExecutionLogsFilter(
    var type: EnumExecutionType? = null,
    var state: EnumExecutionState? = null,
    var endPoint: String? = null,
    var serviceExecutionStart: Pair<LocalDateTime, LocalDateTime>? = null,
    var serviceExecutionEnd: Pair<LocalDateTime, LocalDateTime>? = null,
    var clientExecutionStart: Pair<LocalDateTime, LocalDateTime>? = null,
    var clientExecutionEnd: Pair<LocalDateTime, LocalDateTime>? = null,
    var sort: Sort? = null
)