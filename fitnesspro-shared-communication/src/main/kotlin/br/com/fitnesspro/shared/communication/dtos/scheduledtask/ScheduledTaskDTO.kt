package br.com.fitnesspro.shared.communication.dtos.scheduledtask

import br.com.fitnesspro.shared.communication.dtos.common.AuditableDTO
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

@Schema(description = "Classe DTO usada para manutenção de uma tarefas agendadas")
data class ScheduledTaskDTO(
    @field:Schema(description = "Identificador", example = "e874d31c-0e29-4e9b-b48e-7d70d91b6a16", required = false)
    @field:Size(min = 1, max = 255, message = "baseDTO.id.size")
    override var id: String? = null,

    @field:Schema(description = "Data de criação", example = "2023-01-01T10:00:00", required = false, readOnly = true)
    override var creationDate: LocalDateTime? = null,

    @field:Schema(description = "Data de atualização", example = "2023-01-01T10:00:00", required = false, readOnly = true)
    override var updateDate: LocalDateTime? = null,

    @field:Schema(description = "Valor booleano que representa se o registro está ativo", required = true)
    @field:NotNull(message = "scheduledTaskDTO.active.notNull")
    var active: Boolean? = null,

    @field:Schema(description = "Nome da tarefa", required = true)
    @field:NotNull(message = "scheduledTaskDTO.name.notNull")
    @field:Size(min = 1, max = 255, message = "scheduledTaskDTO.name.size")
    var name: String? = null,

    @field:Schema(description = "Intervalo em milissegundos", required = true)
    @field:NotNull(message = "scheduledTaskDTO.intervalMillis.notNull")
    var intervalMillis: Long? = null,

    @field:Schema(description = "Data da última execução", required = false)
    var lastExecutionTime: LocalDateTime? = null,

    @field:Schema(description = "Nome da chave da classe responsável pela execução", required = true)
    @field:NotNull(message = "scheduledTaskDTO.handlerBeanName.notNull")
    @field:Size(max = 255, message = "scheduledTaskDTO.handlerBeanName.size")
    var handlerBeanName: String? = null,

    @field:Schema(description = "JSON de parâmetros de configuração que o handler pode recuperar e utilizar na execução")
    var configJson: String? = null,
) : AuditableDTO