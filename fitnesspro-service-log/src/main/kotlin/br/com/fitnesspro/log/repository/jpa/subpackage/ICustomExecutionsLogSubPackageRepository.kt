package br.com.fitnesspro.log.repository.jpa.subpackage

import br.com.fitnesspro.models.logs.ExecutionLogSubPackage
import br.com.fitnesspro.shared.communication.paging.PageInfos
import br.com.fitnesspro.shared.communication.query.filter.ExecutionLogSubPackageFilter

interface ICustomExecutionsLogSubPackageRepository {

    fun findSubPackagesByPackageId(packageId: String): List<ExecutionLogSubPackage>

    fun calculateSubPackageInformation(logId: String? = null, packageId: String? = null): SubPackageCalculatedInformation?

    fun getListExecutionLogSubPackage(filter: ExecutionLogSubPackageFilter, pageInfos: PageInfos): List<ExecutionLogSubPackage>

    fun getCountListExecutionLogSubPackage(filter: ExecutionLogSubPackageFilter): Int
}