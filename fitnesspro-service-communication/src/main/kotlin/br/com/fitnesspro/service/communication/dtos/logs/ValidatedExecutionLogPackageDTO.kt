package br.com.fitnesspro.service.communication.dtos.logs

import br.com.fitnesspro.shared.communication.dtos.logs.interfaces.IExecutionLogPackageDTO
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "Classe DTO usada para manutenção de um pacote de log de execução")
data class ValidatedExecutionLogPackageDTO(
    @field:Schema(description = "Identificador", example = "e874d31c-0e29-4e9b-b48e-7d70d91b6a16", required = false)
    override var id: String? = null,

    @field:Schema(description = "Momento que o serviço começou a executar a operação", required = true)
    override val serviceExecutionStart: LocalDateTime? = null,

    @field:Schema(description = "Momento que o serviço terminou de executar a operação", required = false)
    override val serviceExecutionEnd: LocalDateTime? = null,

    @field:Schema(description = "Momento que o client começou a executar a operação", required = false)
    override val clientExecutionStart: LocalDateTime? = null,

    @field:Schema(description = "Momento que o client terminou de executar a operação", required = false)
    override val clientExecutionEnd: LocalDateTime? = null,

    @field:Schema(description = "Request body enviado na requisição", required = false)
    override val requestBody: String? = null,

    @field:Schema(description = "Response body retornado na requisição", required = false)
    override val responseBody: String? = null,

    @field:Schema(description = "Stack de erro", required = false)
    override val error: String? = null,

    @field:Schema(description = "Informações adicionais sobre a execução, onde podem conter logs específicos do processamento realizado.", required = false)
    override val executionAdditionalInfos: String? = null,

    @field:Schema(description = "Identificador do log de execução", required = true)
    override val executionLogId: String? = null,

    @field:Schema(description = "Quantidade de itens inseridos", required = false)
    override var insertedItemsCount: Int? = null,

    @field:Schema(description = "Quantidade de itens atualizados", required = false)
    override var updatedItemsCount: Int? = null,

    @field:Schema(description = "Quantidade de itens enviados na exportação", required = false)
    override var allItemsCount: Int? = null,
): IExecutionLogPackageDTO
