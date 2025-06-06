package br.com.fitnesspro.shared.communication.query.enums

import br.com.fitnesspro.shared.communication.query.sort.IEnumFields

enum class EnumExecutionLogsFields(override val fieldName: String): IEnumFields {
    EXECUTION_TYPE("type"),
    CREATION_DATE("creationDate"),
    EXECUTION_STATE("state"),
    END_POINT("endPoint"),
    METHOD_NAME("methodName"),
    USER_EMAIL("user.email"),
    DEVICE_ID("device.id"),
    APPLICATION_NAME("application.name")
}