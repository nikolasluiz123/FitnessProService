package br.com.fitnesspro.log.repository.jpa.subpackage

import br.com.fitnesspro.models.logs.ExecutionLogSubPackage

interface ICustomExecutionsLogSubPackageRepository {

    fun findSubPackagesByPackageId(packageId: String): List<ExecutionLogSubPackage>

    fun calculateSubPackageInformation(logId: String? = null, packageId: String? = null): SubPackageCalculatedInformation?
}