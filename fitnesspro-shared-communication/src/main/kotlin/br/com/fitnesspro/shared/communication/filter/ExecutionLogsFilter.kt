package br.com.fitnesspro.shared.communication.filter

import br.com.fitnesspro.models.executions.enums.EnumExecutionType
import br.com.fitnesspro.shared.communication.enums.execution.EnumExecutionState
import java.time.LocalDate

data class ExecutionLogsFilter(
    var type: EnumExecutionType? = null,
    var state: EnumExecutionState? = null,
    var endPoint: String? = null,
    var executionStart: Pair<LocalDate, LocalDate>? = null,
    var executionEnd: Pair<LocalDate, LocalDate>? = null,
)