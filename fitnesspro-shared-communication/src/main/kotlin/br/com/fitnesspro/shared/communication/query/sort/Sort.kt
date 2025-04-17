package br.com.fitnesspro.shared.communication.query.sort

data class Sort<T>(
    var field: T,
    var asc: Boolean
) where T : Enum<T>, T : IEnumFields
