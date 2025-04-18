package br.com.fitnesspro.shared.communication.query.filter

import br.com.fitnesspro.shared.communication.query.enums.EnumAcademyFields
import br.com.fitnesspro.shared.communication.query.sort.Sort

data class AcademyFilter(
    var name: String? = null,
    var address: String? = null,
    var phone: String? = null,
    var sort: Sort<EnumAcademyFields>? = null,
)
