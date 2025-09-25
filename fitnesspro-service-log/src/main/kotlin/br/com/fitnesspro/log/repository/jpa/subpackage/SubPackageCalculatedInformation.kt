package br.com.fitnesspro.log.repository.jpa.subpackage

data class SubPackageCalculatedInformation(
    var insertedItemsCount: Long = 0,
    var updatedItemsCount: Long = 0,
    var allItemsCount: Long = 0,
    var kbSize: Long = 0,
)