package br.com.fitnesspro.shared.communication.dtos.logs

import br.com.fitnesspro.shared.communication.dtos.logs.interfaces.IExecutionLogSubPackageDTO
import java.time.LocalDateTime

data class ExecutionLogSubPackageDTO(
    override var id: String? = null,
    override val entityName: String? = null,
    override val executionLogPackageId: String? = null,
    override val insertedItemsCount: Int? = null,
    override val updatedItemsCount: Int? = null,
    override val allItemsCount: Int? = null,
    override val kbSize: Long? = null,
    override val lastUpdateDate: LocalDateTime? = null
) : IExecutionLogSubPackageDTO