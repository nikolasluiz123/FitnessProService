package br.com.fitnesspro.shared.communication.query.enums

import br.com.fitnesspro.shared.communication.query.sort.IEnumFields

enum class EnumPersonFields(override val fieldName: String): IEnumFields {
    NAME("name"),
    EMAIL("email"),
    USER_TYPE("type"),
    CREATION_DATE("creationDate") {
        override fun getDBColumn(): String = "creation_date"
    },
    UPDATE_DATE("updateDate") {
        override fun getDBColumn(): String = "update_date"
    },
}