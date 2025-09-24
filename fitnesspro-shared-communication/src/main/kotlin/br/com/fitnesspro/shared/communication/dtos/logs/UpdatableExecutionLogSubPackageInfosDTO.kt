package br.com.fitnesspro.shared.communication.dtos.logs

import br.com.fitnesspro.shared.communication.dtos.logs.interfaces.IUpdatableExecutionLogSubPackageEntityCountsDTO
import br.com.fitnesspro.shared.communication.dtos.logs.interfaces.IUpdatableExecutionLogSubPackageInfosDTO
import java.time.LocalDateTime

data class UpdatableExecutionLogSubPackageInfosDTO(
    override val entityCounts: Map<String, IUpdatableExecutionLogSubPackageEntityCountsDTO>,
    override val lastUpdateDateMap: Map<String, LocalDateTime?>
) : IUpdatableExecutionLogSubPackageInfosDTO
