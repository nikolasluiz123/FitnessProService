package br.com.fitnesspro.shared.communication.dtos.logs.interfaces

import br.com.fitnesspro.shared.communication.dtos.common.BaseDTO
import java.time.LocalDateTime

interface IExecutionLogSubPackageDTO : BaseDTO {
    val entityName: String?
    val executionLogPackageId: String?
    val insertedItemsCount: Int?
    val updatedItemsCount: Int?
    val allItemsCount: Int?
    val kbSize: Long?
    val lastUpdateDate: LocalDateTime?
}