package br.com.fitnesspro.shared.communication.query.enums

import br.com.fitnesspro.shared.communication.query.sort.IEnumFields

enum class EnumDeviceFields(override val fieldName: String): IEnumFields {
    ID("id"),
    BRAND("brand"),
    ANDROID_VERSION("androidVersion"),
    PERSON_NAME("person.name"),
    CREATION_DATE("creationDate"),
    UPDATE_DATE("updateDate"),
    MODEL("model"),
}