package br.com.fitnesspro.shared.communication.dtos.logs

import br.com.fitnesspro.shared.communication.dtos.common.BaseDTO
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class ExecutionLogPackageDTO(
    @Schema(description = "Identificador", example = "e874d31c-0e29-4e9b-b48e-7d70d91b6a16", required = false)
    override var id: String? = null,

    @Schema(description = "Momento que o serviço começou a executar a operação", required = true)
    val serviceExecutionStart: LocalDateTime? = null,

    @Schema(description = "Momento que o serviço terminou de executar a operação", required = false)
    val serviceExecutionEnd: LocalDateTime? = null,

    @Schema(description = "Momento que o client começou a executar a operação", required = false)
    val clientExecutionStart: LocalDateTime? = null,

    @Schema(description = "Momento que o client terminou de executar a operação", required = false)
    val clientExecutionEnd: LocalDateTime? = null,

    @Schema(description = "Request body enviado na requisição", required = false)
    val requestBody: String? = null,

    @Schema(description = "Stack de erro", required = false)
    val error: String? = null,

    @Schema(description = "Identificador do log de execução", required = true)
    val executionLogId: String? = null,

    @Schema(description = "Quantidade de itens inseridos", required = false)
    val insertedItemsCount: Int? = null,

    @Schema(description = "Quantidade de itens atualizados", required = false)
    val updatedItemsCount: Int? = null,

    @Schema(description = "Quantidade de itens enviados na exportação", required = false)
    val allItemsCount: Int? = null,
): BaseDTO
