package br.com.fitnesspro.shared.communication.dtos.scheduledtask

import br.com.fitnesspro.shared.communication.dtos.common.AuditableDTO
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

data class ScheduledTaskDTO(
    @Schema(description = "Identificador", example = "e874d31c-0e29-4e9b-b48e-7d70d91b6a16", required = false)
    @field:Size(min = 1, max = 255, message = "O identificador deve ter entre 1 e 255 caracteres")
    override var id: String? = null,

    @Schema(description = "Data de criação", example = "2023-01-01T10:00:00", required = false, readOnly = true)
    override var creationDate: LocalDateTime? = null,

    @Schema(description = "Data de atualização", example = "2023-01-01T10:00:00", required = false, readOnly = true)
    override var updateDate: LocalDateTime? = null,

    @Schema(description = "Valor booleano que representa se o registro está ativo", required = true)
    @field:NotNull(message = "O campo ativo é obrigatório")
    var active: Boolean? = null,

    @Schema(description = "Nome da tarefa", required = true)
    @field:NotNull(message = "O nome da tarefa é obrigatório")
    var name: String? = null,

    @Schema(description = "Intervalo em milissegundos", required = true)
    @field:NotNull(message = "O intervalo em milissegundos é obrigatório")
    var intervalMillis: Long? = null,

    @Schema(description = "Data da última execução", required = false)
    var lastExecutionTime: LocalDateTime? = null,

    @Schema(description = "Nome da chave da classe responsável pela execução", required = true)
    var handlerBeanName: String? = null,

    @Schema(
        description = "JSON de parâmetros de configuração que o handler pode recuperar e utilizar na execução",
        required = true
    )
    var configJson: String? = null,
) : AuditableDTO