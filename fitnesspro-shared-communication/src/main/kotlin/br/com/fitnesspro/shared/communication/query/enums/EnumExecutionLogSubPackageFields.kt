package br.com.fitnesspro.shared.communication.query.enums

import br.com.fitnesspro.shared.communication.query.sort.IEnumFields

enum class EnumExecutionLogSubPackageFields(override val fieldName: String): IEnumFields {
    ENTITY_NAME("entityName"),
    INSERTED_ITEMS_COUNT("insertedItemsCount"),
    UPDATED_ITEMS_COUNT("updatedItemsCount"),
    ALL_ITEMS_COUNT("allItemsCount"),
    KB_SIZE("kbSize"),
    LAST_UPDATE_DATE("lastUpdateDate")
}