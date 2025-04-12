package br.com.fitnesspro.shared.communication.query.filter

import br.com.fitnesspro.shared.communication.query.sort.Sort

data class ApplicationFilter(
    var name: String? = null,
    var sort: Sort? = null
)
