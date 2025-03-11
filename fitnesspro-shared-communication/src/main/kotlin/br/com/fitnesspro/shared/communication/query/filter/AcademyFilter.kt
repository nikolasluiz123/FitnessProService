package br.com.fitnesspro.shared.communication.query.filter

import br.com.fitnesspro.shared.communication.query.sort.Sort

data class AcademyFilter(
    var name: String? = null,
    var address: String? = null,
    var onlyActives: Boolean = true,
    var sort: Sort? = null,
)
