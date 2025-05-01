package br.com.fitnesspro.shared.communication.query.enums

import br.com.fitnesspro.shared.communication.query.sort.IEnumFields

enum class EnumScheduledTaskFields(override val fieldName: String): IEnumFields {
    NAME("name"),
    TYPE("type"),
    LAST_EXECUTION_TIME("lastExecutionTime"),
    CREATION_DATE("creationDate"),
    UPDATE_DATE("updateDate")
}