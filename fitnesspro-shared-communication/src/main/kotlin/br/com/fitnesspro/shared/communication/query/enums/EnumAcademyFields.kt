package br.com.fitnesspro.shared.communication.query.enums

import br.com.fitnesspro.shared.communication.query.sort.IEnumFields

enum class EnumAcademyFields(override val fieldName: String): IEnumFields {
    CREATION_DATE("creationDate"),
    UPDATE_DATE("updateDate"),
    NAME("name"),
    ADDRESS("address"),
    PHONE("phone")
}