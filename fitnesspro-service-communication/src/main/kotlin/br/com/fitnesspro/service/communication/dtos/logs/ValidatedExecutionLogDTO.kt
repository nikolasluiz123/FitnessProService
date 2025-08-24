package br.com.fitnesspro.service.communication.dtos.logs

import br.com.fitnesspro.shared.communication.dtos.logs.interfaces.IExecutionLogDTO
import br.com.fitnesspro.shared.communication.enums.execution.EnumExecutionState
import br.com.fitnesspro.shared.communication.enums.execution.EnumExecutionType
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "Classe DTO usada para leitura de um log de execução")
data class ValidatedExecutionLogDTO(
    @field:Schema(description = "Identificador", example = "e874d31c-0e29-4e9b-b48e-7d70d91b6a16", required = false)
    override var id: String? = null,

    @field:Schema(description = "Tipo de execução", required = true)
    override val type: EnumExecutionType? = null,

    @field:Schema(description = "Estado da execução", required = true)
    override val state: EnumExecutionState? = null,

    @field:Schema(description = "End point que foi chamado", required = true)
    override val endPoint: String? = null,

    @field:Schema(description = "Nome do método da classe Controller que foi invocado", required = true)
    override val methodName: String? = null,

    @field:Schema(description = "E-mail do usuário que disparou a execução", required = false)
    override val userEmail: String? = null,

    @field:Schema(description = "Id do dispositivo que disparou a execução", required = false)
    override val deviceId: String? = null,

    @field:Schema(description = "Nome da aplicação que disparou a execução", required = false)
    override val applicationName: String? = null,

    @field:Schema(description = "Quantidade de itens por página", required = false)
    override var pageSize: Int? = null,

    @field:Schema(description = "Data da última atualização. Utilizado na importação de dados.", required = false)
    override var lastUpdateDate: LocalDateTime? = null,

    @field:Schema(description = "Data de criação do log de execução", required = true)
    override var creationDate: LocalDateTime? = null
): IExecutionLogDTO
