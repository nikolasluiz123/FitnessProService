package br.com.fitnesspro.shared.communication.query.filter

import br.com.fitnesspro.shared.communication.enums.execution.EnumExecutionState
import br.com.fitnesspro.shared.communication.enums.execution.EnumExecutionType
import java.time.LocalDateTime

data class ExecutionLogsFilter(
    var type: EnumExecutionType? = null,
    var creationDate: Pair<LocalDateTime, LocalDateTime>? = null,
    var state: EnumExecutionState? = null,
    var endPoint: String? = null,
    var methodName: String? = null,
    var userEmail: String? = null,
    var deviceId: String? = null,
    var applicationName: String? = null
)