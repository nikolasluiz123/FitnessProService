package br.com.fitnesspro.shared.communication.dtos.logs.interfaces

import java.time.LocalDateTime

interface IUpdatableExecutionLogPackageInfosDTO {
    var insertedItemsCount: Int?
    var updatedItemsCount: Int?
    var allItemsCount: Int?
    var clientExecutionStart: LocalDateTime?
    var clientExecutionEnd: LocalDateTime?
    var error: String?
}