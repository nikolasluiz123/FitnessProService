package br.com.fitnesspro.shared.communication.query.sort

import java.io.Serializable

interface IEnumFields: Serializable {
    val fieldName: String

    fun getDBColumn(): String = fieldName
}