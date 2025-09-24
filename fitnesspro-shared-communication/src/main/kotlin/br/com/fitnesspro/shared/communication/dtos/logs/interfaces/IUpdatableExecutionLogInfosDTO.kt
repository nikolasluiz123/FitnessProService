package br.com.fitnesspro.shared.communication.dtos.logs.interfaces

import br.com.fitnesspro.shared.communication.enums.execution.EnumExecutionState

interface IUpdatableExecutionLogInfosDTO {
    var pageSize: Int?
    var state: EnumExecutionState?
}