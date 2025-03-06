package br.com.fitnesspro.shared.communication.dtos.logs

import br.com.fitnesspro.models.executions.enums.EnumExecutionType
import br.com.fitnesspro.shared.communication.dtos.common.BaseDTO
import br.com.fitnesspro.shared.communication.enums.execution.EnumExecutionState
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class ExecutionLogDTO(
    @Schema(description = "Identificador", example = "e874d31c-0e29-4e9b-b48e-7d70d91b6a16", required = false, readOnly = true)
    override var id: String? = null,

    @Schema(description = "Tipo de execução", required = true, readOnly = true)
    val type: EnumExecutionType? = null,

    @Schema(description = "Estado da execução", required = true, readOnly = true)
    val state: EnumExecutionState? = null,

    @Schema(description = "Momento que o serviço começou a executar a operação", required = true, readOnly = true)
    val serviceExecutionStart: LocalDateTime? = null,

    @Schema(description = "Momento que o serviço terminou de executar a operação", required = false, readOnly = true)
    val serviceExecutionEnd: LocalDateTime? = null,

    @Schema(description = "Momento que o client começou a executar a operação. Nem toda execução vai registrar isso.", required = false, readOnly = true)
    var clientExecutionStart: LocalDateTime? = null,

    @Schema(description = "Momento que o client terminou de executar a operação. Nem toda execução vai registrar isso.", required = false, readOnly = true)
    var clientExecutionEnd: LocalDateTime? = null,

    @Schema(description = "End point que foi chamado", required = true, readOnly = true)
    val endPoint: String? = null,

    @Schema(description = "Nome do método da classe Controller que foi invocado", required = true, readOnly = true)
    val methodName: String? = null,

    @Schema(description = "Request body enviado na requisição", required = false, readOnly = true)
    val requestBody: String? = null,

    @Schema(description = "Stack de erro", required = false, readOnly = true)
    val error: String? = null
): BaseDTO
