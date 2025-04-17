package br.com.fitnesspro.shared.communication.query.filter

import br.com.fitnesspro.shared.communication.enums.general.EnumUserType
import br.com.fitnesspro.shared.communication.query.enums.EnumPersonFields
import br.com.fitnesspro.shared.communication.query.sort.Sort
import java.time.LocalDateTime

data class PersonFilter(
    var name: String? = null,
    var email: String? = null,
    var creationDate: Pair<LocalDateTime, LocalDateTime>? = null,
    var updateDate: Pair<LocalDateTime, LocalDateTime>? = null,
    var userType: EnumUserType? = null,
    var sort: List<Sort<EnumPersonFields>> = emptyList()
)
