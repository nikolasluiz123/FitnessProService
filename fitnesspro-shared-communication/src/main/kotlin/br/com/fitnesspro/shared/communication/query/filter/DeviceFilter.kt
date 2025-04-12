package br.com.fitnesspro.shared.communication.query.filter

import br.com.fitnesspro.shared.communication.query.sort.Sort
import java.time.LocalDateTime

data class DeviceFilter(
    var id: String? = null,
    var model: String? = null,
    var creationDate: Pair<LocalDateTime, LocalDateTime>? = null,
    var updateDate: Pair<LocalDateTime, LocalDateTime>? = null,
    var sort: Sort? = null
)
