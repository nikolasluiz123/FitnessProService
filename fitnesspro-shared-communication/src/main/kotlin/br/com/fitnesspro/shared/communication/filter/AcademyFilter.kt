package br.com.fitnesspro.shared.communication.filter

data class AcademyFilter(
    var name: String? = null,
    var address: String? = null,
    var onlyActives: Boolean = true,
    var sort: Sort? = null,
)
