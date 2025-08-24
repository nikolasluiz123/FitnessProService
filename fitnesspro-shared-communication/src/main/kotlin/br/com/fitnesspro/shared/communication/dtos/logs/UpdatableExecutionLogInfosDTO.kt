package br.com.fitnesspro.shared.communication.dtos.logs

import br.com.fitnesspro.shared.communication.dtos.logs.interfaces.IUpdatableExecutionLogInfosDTO
import br.com.fitnesspro.shared.communication.enums.execution.EnumExecutionState
import java.time.LocalDateTime

data class UpdatableExecutionLogInfosDTO(
    override var lastUpdateDate: LocalDateTime? = null,
    override var pageSize: Int? = null,
    override var state: EnumExecutionState? = null
) : IUpdatableExecutionLogInfosDTO
