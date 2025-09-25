package br.com.fitnesspro.shared.communication.dtos.logs.interfaces

import br.com.fitnesspro.shared.communication.dtos.common.BaseDTO
import java.time.LocalDateTime

interface IExecutionLogPackageDTO : BaseDTO {
    val serviceExecutionStart: LocalDateTime?
    val serviceExecutionEnd: LocalDateTime?
    val clientExecutionStart: LocalDateTime?
    val clientExecutionEnd: LocalDateTime?
    val requestBody: String?
    val responseBody: String?
    val error: String?
    val executionAdditionalInfos: String?
    val executionLogId: String?
    val insertedItemsCount: Int?
    val updatedItemsCount: Int?
    val allItemsCount: Int?
    val kbSize: Long
    val serviceProcessingDuration: Long?
    val clientProcessingDuration: Long?
}