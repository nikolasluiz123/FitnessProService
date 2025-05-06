package br.com.fitnesspro.shared.communication.query.enums

import br.com.fitnesspro.shared.communication.query.sort.IEnumFields

enum class EnumServiceTokenFields(override val fieldName: String): IEnumFields {
    TYPE("type"),
    CREATION_DATE("creationDate"),
    EXPIRATION_DATE("expirationDate"),
    USER_EMAIL("user.email"),
    DEVICE_ID("device.id"),
    APPLICATION_NAME("application.name")
}