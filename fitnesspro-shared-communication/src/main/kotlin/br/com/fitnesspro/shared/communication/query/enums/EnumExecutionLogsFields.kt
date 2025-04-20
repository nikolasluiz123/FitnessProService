package br.com.fitnesspro.shared.communication.query.enums

import br.com.fitnesspro.shared.communication.query.sort.IEnumFields

enum class EnumExecutionLogsFields(override val fieldName: String): IEnumFields {
    EXECUTION_TYPE("type"),
    EXECUTION_STATE("state"),
    END_POINT("endPoint"),
    METHOD_NAME("methodName"),
    USER_EMAIL("userEmail"),
    DEVICE_ID("id"),
    APPLICATION_NAME("name")
}