package br.com.fitnesspro.service.communication.dtos.scheduler

import br.com.fitnesspro.shared.communication.dtos.scheduler.interfaces.ISchedulerConfigDTO
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

@Schema(description = "Classe DTO usada para manutenção de uma configuração dos agendamentos da pessoa")
data class ValidatedSchedulerConfigDTO(
    @field:Schema(description = "Identificador", example = "e874d31c-0e29-4e9b-b48e-7d70d91b6a16", required = false)
    @field:Size(min = 1, max = 255, message = "baseDTO.id.size")
    override var id: String? = null,

    @field:Schema(description = "Data de criação", example = "2023-01-01T10:00:00", required = false, readOnly = true)
    override var creationDate: LocalDateTime? = null,

    @field:Schema(description = "Data de atualização", example = "2023-01-01T10:00:00", required = false, readOnly = true)
    override var updateDate: LocalDateTime? = null,

    @field:Schema(description = "Valor booleano que representa se a notificação está ativada", required = true)
    @field:NotNull(message = "schedulerConfigDTO.notification.notNull")
    override var notification: Boolean = false,

    @field:Schema(description = "Tempo de antecedência da notificação em minutos", example = "30", required = true)
    @field:NotNull(message = "schedulerConfigDTO.notificationAntecedenceTime.notNull")
    @field:Min(value = 30, message = "schedulerConfigDTO.notificationAntecedenceTime.min")
    override var notificationAntecedenceTime: Int = 30,

    @field:Schema(description = "Densidade mínima de agendamento", example = "1", required = true)
    @field:Min(value = 1, message = "schedulerConfigDTO.minScheduleDensity.min")
    override var minScheduleDensity: Int = 1,

    @field:Schema(description = "Densidade máxima de agendamento", example = "2", required = true)
    @field:Min(value = 1, message = "schedulerConfigDTO.maxScheduleDensity.min")
    override var maxScheduleDensity: Int = 2,

    @field:Schema(description = "Identificador da pessoa", example = "e874d31c-0e29-4e9b-b48e-7d70d91b6a16", required = true)
    @field:NotNull(message = "schedulerConfigDTO.personId.notNull")
    override var personId: String? = null
) : ISchedulerConfigDTO