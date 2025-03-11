package br.com.fitnesspro.shared.communication.query.enums

import br.com.fitnesspro.shared.communication.query.sort.IEnumFields

enum class EnumExecutionLogsPackageFields(override val fieldName: String): IEnumFields {
    SERVICE_EXECUTION_START("serviceExecutionStart"),
    SERVICE_EXECUTION_END("serviceExecutionEnd"),
    CLIENT_EXECUTION_START("clientExecutionStart"),
    CLIENT_EXECUTION_END("clientExecutionEnd"),
    INSERTED_ITEMS_COUNT("insertedItemsCount"),
    UPDATED_ITEMS_COUNT("updatedItemsCount"),
    ALL_ITEMS_COUNT("allItemsCount")
}