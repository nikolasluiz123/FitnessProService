package br.com.fitnesspro.log.repository.jpa

import br.com.fitnesspro.models.logs.ExecutionLogSubPackage

interface ICustomExecutionsLogSubPackageRepository {

    fun findSubPackagesByPackageId(packageId: String): List<ExecutionLogSubPackage>

    fun getCountInsertedItemsFromPackage(packageId: String): Int

    fun getCountUpdatedItemsFromPackage(packageId: String): Int

    fun getCountProcessedItemsFromPackage(packageId: String): Int
}