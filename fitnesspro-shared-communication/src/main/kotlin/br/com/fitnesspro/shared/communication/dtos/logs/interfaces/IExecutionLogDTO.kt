package br.com.fitnesspro.shared.communication.dtos.logs.interfaces

import br.com.fitnesspro.shared.communication.dtos.common.BaseDTO
import br.com.fitnesspro.shared.communication.enums.execution.EnumExecutionState
import br.com.fitnesspro.shared.communication.enums.execution.EnumExecutionType
import java.time.LocalDateTime

interface IExecutionLogDTO : BaseDTO {
    val type: EnumExecutionType?
    val state: EnumExecutionState?
    val endPoint: String?
    val methodName: String?
    val userEmail: String?
    val deviceId: String?
    val applicationName: String?
    var pageSize: Int?
    var lastUpdateDate: LocalDateTime?
    var creationDate: LocalDateTime?
}