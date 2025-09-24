package br.com.fitnesspro.shared.communication.dtos.logs

import br.com.fitnesspro.shared.communication.dtos.logs.interfaces.IUpdatableExecutionLogPackageInfosDTO
import java.time.LocalDateTime

data class UpdatableExecutionLogPackageInfosDTO(
    override var clientExecutionStart: LocalDateTime? = null,
    override var clientExecutionEnd: LocalDateTime? = null,
    override var error: String? = null
) : IUpdatableExecutionLogPackageInfosDTO
