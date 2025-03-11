package br.com.fitnesspro.shared.communication.dtos.logs

import br.com.fitnesspro.models.executions.enums.EnumExecutionType
import br.com.fitnesspro.shared.communication.dtos.common.BaseDTO
import br.com.fitnesspro.shared.communication.enums.execution.EnumExecutionState
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class ExecutionLogDTO(
    @Schema(description = "Identificador", example = "e874d31c-0e29-4e9b-b48e-7d70d91b6a16", required = false)
    override var id: String? = null,

    @Schema(description = "Tipo de execução", required = true)
    val type: EnumExecutionType? = null,

    @Schema(description = "Estado da execução", required = true)
    val state: EnumExecutionState? = null,

    @Schema(description = "End point que foi chamado", required = true)
    val endPoint: String? = null,

    @Schema(description = "Nome do método da classe Controller que foi invocado", required = true)
    val methodName: String? = null,

    @Schema(description = "E-mail do usuário que disparou a execução", required = true)
    val userEmail: String?,

    @Schema(description = "Quantidade de itens por página", required = false)
    var pageSize: Int? = null,

    @Schema(description = "Data da última atualização. Utilizado na importação de dados.", required = false)
    var lastUpdateDate: LocalDateTime? = null,
): BaseDTO
