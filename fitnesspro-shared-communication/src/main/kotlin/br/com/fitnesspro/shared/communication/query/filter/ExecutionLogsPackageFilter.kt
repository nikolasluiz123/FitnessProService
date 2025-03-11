package br.com.fitnesspro.shared.communication.query.filter

import br.com.fitnesspro.shared.communication.query.sort.Sort
import java.time.LocalDateTime

data class ExecutionLogsPackageFilter(
    var executionLogId: String? = null,
    var clientExecutionStart: Pair<LocalDateTime, LocalDateTime>? = null,
    var clientExecutionEnd: Pair<LocalDateTime, LocalDateTime>? = null,
    var serviceExecutionStart: Pair<LocalDateTime, LocalDateTime>? = null,
    var serviceExecutionEnd: Pair<LocalDateTime, LocalDateTime>? = null,
    var sort: Sort? = null
)