package br.com.fitnesspro.shared.communication.dtos.logs

import br.com.fitnesspro.shared.communication.dtos.logs.interfaces.IExecutionLogPackageDTO
import java.time.LocalDateTime

data class ExecutionLogPackageDTO(
    override var id: String? = null,
    override val serviceExecutionStart: LocalDateTime? = null,
    override val serviceExecutionEnd: LocalDateTime? = null,
    override val clientExecutionStart: LocalDateTime? = null,
    override val clientExecutionEnd: LocalDateTime? = null,
    override val requestBody: String? = null,
    override val responseBody: String? = null,
    override val error: String? = null,
    override val executionAdditionalInfos: String? = null,
    override val executionLogId: String? = null,
    override val insertedItemsCount: Int? = null,
    override val updatedItemsCount: Int? = null,
    override val allItemsCount: Int? = null,
    override val kbSize: Long = 0,
    override val serviceProcessingDuration: Long? = null,
    override val clientProcessingDuration: Long? = null
) : IExecutionLogPackageDTO
