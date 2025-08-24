package br.com.fitnesspro.shared.communication.dtos.logs

import br.com.fitnesspro.shared.communication.dtos.logs.interfaces.IExecutionLogDTO
import br.com.fitnesspro.shared.communication.enums.execution.EnumExecutionState
import br.com.fitnesspro.shared.communication.enums.execution.EnumExecutionType
import java.time.LocalDateTime

data class ExecutionLogDTO(
    override var id: String? = null,
    override val type: EnumExecutionType? = null,
    override val state: EnumExecutionState? = null,
    override val endPoint: String? = null,
    override val methodName: String? = null,
    override val userEmail: String? = null,
    override val deviceId: String? = null,
    override val applicationName: String? = null,
    override var pageSize: Int? = null,
    override var lastUpdateDate: LocalDateTime? = null,
    override var creationDate: LocalDateTime? = null
) : IExecutionLogDTO
