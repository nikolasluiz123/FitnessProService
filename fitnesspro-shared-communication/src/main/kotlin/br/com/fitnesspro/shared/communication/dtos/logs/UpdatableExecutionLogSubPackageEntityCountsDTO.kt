package br.com.fitnesspro.shared.communication.dtos.logs

import br.com.fitnesspro.shared.communication.dtos.logs.interfaces.IUpdatableExecutionLogSubPackageEntityCountsDTO

data class UpdatableExecutionLogSubPackageEntityCountsDTO(
    override var insertedItemsCount: Int = 0,
    override var updatedItemsCount: Int = 0,
) : IUpdatableExecutionLogSubPackageEntityCountsDTO
