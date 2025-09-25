package br.com.fitnesspro.shared.communication.query.filter

import br.com.fitnesspro.shared.communication.query.enums.EnumExecutionLogSubPackageFields
import br.com.fitnesspro.shared.communication.query.sort.Sort

data class ExecutionLogSubPackageFilter(
    var entityName: String? = null,
    var executionLogPackageId: String? = null,
    var sort: List<Sort<EnumExecutionLogSubPackageFields>> = emptyList()
)