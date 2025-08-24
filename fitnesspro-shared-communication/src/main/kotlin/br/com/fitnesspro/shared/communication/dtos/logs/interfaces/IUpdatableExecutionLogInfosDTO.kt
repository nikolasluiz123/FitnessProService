package br.com.fitnesspro.shared.communication.dtos.logs.interfaces

import br.com.fitnesspro.shared.communication.enums.execution.EnumExecutionState
import java.time.LocalDateTime

interface IUpdatableExecutionLogInfosDTO {
    var lastUpdateDate: LocalDateTime?
    var pageSize: Int?
    var state: EnumExecutionState?
}