package br.com.fitnesspro.shared.communication.query.filter

import br.com.fitnesspro.shared.communication.enums.execution.EnumExecutionType
import br.com.fitnesspro.shared.communication.enums.execution.EnumExecutionState

data class ExecutionLogsFilter(
    var type: EnumExecutionType? = null,
    var state: EnumExecutionState? = null,
    var endPoint: String? = null,
    var methodName: String? = null,
    var userEmail: String? = null
)