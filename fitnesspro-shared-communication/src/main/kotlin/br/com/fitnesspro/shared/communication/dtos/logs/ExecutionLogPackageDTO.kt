package br.com.fitnesspro.shared.communication.dtos.logs

import br.com.fitnesspro.shared.communication.dtos.common.BaseDTO
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "Classe DTO usada para manutenção de um pacote de log de execução")
data class ExecutionLogPackageDTO(
    @field:Schema(description = "Identificador", example = "e874d31c-0e29-4e9b-b48e-7d70d91b6a16", required = false)
    override var id: String? = null,

    @field:Schema(description = "Momento que o serviço começou a executar a operação", required = true)
    val serviceExecutionStart: LocalDateTime? = null,

    @field:Schema(description = "Momento que o serviço terminou de executar a operação", required = false)
    val serviceExecutionEnd: LocalDateTime? = null,

    @field:Schema(description = "Momento que o client começou a executar a operação", required = false)
    val clientExecutionStart: LocalDateTime? = null,

    @field:Schema(description = "Momento que o client terminou de executar a operação", required = false)
    val clientExecutionEnd: LocalDateTime? = null,

    @field:Schema(description = "Request body enviado na requisição", required = false)
    val requestBody: String? = null,

    @field:Schema(description = "Response body retornado na requisição", required = false)
    val responseBody: String? = null,

    @field:Schema(description = "Stack de erro", required = false)
    val error: String? = null,

    @field:Schema(description = "Informações adicionais sobre a execução, onde podem conter logs específicos do processamento realizado.", required = false)
    val executionAdditionalInfos: String? = null,

    @field:Schema(description = "Identificador do log de execução", required = true)
    val executionLogId: String? = null,

    @field:Schema(description = "Quantidade de itens inseridos", required = false)
    val insertedItemsCount: Int? = null,

    @field:Schema(description = "Quantidade de itens atualizados", required = false)
    val updatedItemsCount: Int? = null,

    @field:Schema(description = "Quantidade de itens enviados na exportação", required = false)
    val allItemsCount: Int? = null,
): BaseDTO
