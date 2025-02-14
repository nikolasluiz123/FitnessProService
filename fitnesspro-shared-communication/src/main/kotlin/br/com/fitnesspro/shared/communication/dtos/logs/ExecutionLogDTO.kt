package br.com.fitnesspro.shared.communication.dtos.logs

import br.com.fitnesspro.shared.communication.dtos.common.BaseDTO
import br.com.fitnesspro.models.executions.enums.EnumExecutionState
import br.com.fitnesspro.models.executions.enums.EnumExecutionType
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class ExecutionLogDTO(
    @Schema(description = "Identificador", example = "e874d31c-0e29-4e9b-b48e-7d70d91b6a16", required = false, readOnly = true)
    override var id: String? = null,

    @Schema(description = "Tipo de execução", required = true, readOnly = true)
    val type: EnumExecutionType? = null,

    @Schema(description = "Estado da execução", required = true, readOnly = true)
    val state: EnumExecutionState? = null,

    @Schema(description = "Data que a execução começou", required = true, readOnly = true)
    val executionStart: LocalDateTime? = null,

    @Schema(description = "Data que a execução terminou", required = false, readOnly = true)
    val executionEnd: LocalDateTime? = null,

    @Schema(description = "End poin que foi chamado", required = true, readOnly = true)
    val endPoint: String? = null,

    @Schema(description = "Request body enviado na requisição", required = false, readOnly = true)
    val requestBody: String? = null,

    @Schema(description = "Stack de erro", required = false, readOnly = true)
    val error: String? = null
): BaseDTO
