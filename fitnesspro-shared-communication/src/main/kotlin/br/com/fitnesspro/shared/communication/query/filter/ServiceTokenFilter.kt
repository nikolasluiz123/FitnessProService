package br.com.fitnesspro.shared.communication.query.filter

import br.com.fitnesspro.shared.communication.enums.serviceauth.EnumTokenType
import br.com.fitnesspro.shared.communication.query.enums.EnumServiceTokenFields
import br.com.fitnesspro.shared.communication.query.sort.Sort
import java.time.LocalDateTime

data class ServiceTokenFilter(
    var creationDate: Pair<LocalDateTime, LocalDateTime>? = null,
    var expirationDate: Pair<LocalDateTime, LocalDateTime>? = null,
    var tokenType: EnumTokenType? = null,
    var userEmail: String? = null,
    var deviceId: String? = null,
    var applicationName: String? = null,
    var sort: List<Sort<EnumServiceTokenFields>> = emptyList()
)
