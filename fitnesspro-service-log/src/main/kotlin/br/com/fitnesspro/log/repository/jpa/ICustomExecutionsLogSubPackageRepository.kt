package br.com.fitnesspro.log.repository.jpa

import br.com.fitnesspro.models.logs.ExecutionLogSubPackage

interface ICustomExecutionsLogSubPackageRepository {

    fun findSubPackagesByPackageId(packageId: String): List<ExecutionLogSubPackage>
}