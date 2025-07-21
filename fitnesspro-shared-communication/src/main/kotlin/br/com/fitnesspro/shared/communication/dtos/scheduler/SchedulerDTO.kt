package br.com.fitnesspro.shared.communication.dtos.scheduler

import br.com.fitnesspro.shared.communication.dtos.common.AuditableDTO
import br.com.fitnesspro.shared.communication.enums.scheduler.EnumCompromiseType
import br.com.fitnesspro.shared.communication.enums.scheduler.EnumSchedulerSituation
import br.com.fitnesspro.shared.communication.enums.scheduler.EnumSchedulerType
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDateTime
import java.time.OffsetDateTime

@Schema(description = "Classe DTO usada para manutenção de um agendamento")
data class SchedulerDTO(
    @Schema(description = "Identificador", example = "e874d31c-0e29-4e9b-b48e-7d70d91b6a16", required = false)
    @field:Size(min = 1, max = 255, message = "baseDTO.id.size")
    override var id: String? = null,

    @Schema(description = "Data de criação", example = "2023-01-01T10:00:00", required = false, readOnly = true)
    override var creationDate: LocalDateTime? = null,

    @Schema(description = "Data de atualização", example = "2023-01-01T10:00:00", required = false, readOnly = true)
    override var updateDate: LocalDateTime? = null,

    @Schema(description = "Valor booleano que representa se o registro está ativo", required = true)
    @field:NotNull(message = "schedulerDTO.active.notNull")
    var active: Boolean = true,

    @Schema(description = "Identificador do membro da academia", example = "e874d31c-0e29-4e9b-b48e-7d70d91b6a16", required = true)
    @field:NotNull(message = "schedulerDTO.academyMemberPersonId.notNull")
    var academyMemberPersonId: String? = null,

    @Schema(description = "Identificador do profissional", example = "e874d31c-0e29-4e9b-b48e-7d70d91b6a16", required = true)
    @field:NotNull(message = "schedulerDTO.professionalPersonId.notNull")
    var professionalPersonId: String? = null,

    @Schema(description = "Hora de início", example = "2025-05-10T10:15:30-03:00", required = true)
    @field:NotNull(message = "schedulerDTO.timeStart.notNull")
    var dateTimeStart: OffsetDateTime? = null,

    @Schema(description = "Hora de término", example = "2025-05-10T10:15:30-03:00", required = true)
    @field:NotNull(message = "schedulerDTO.timeEnd.notNull")
    var dateTimeEnd: OffsetDateTime? = null,

    @Schema(description = "Data de cancelamento", example = "2025-05-10T10:15:30-03:00", required = false)
    var canceledDate: OffsetDateTime? = null,

    @Schema(description = "Identificador da pessoa que cancelou o agendamento", example = "e874d31c-0e29-4e9b-b48e-7d70d9", required = false)
    var cancellationPersonId: String? = null,

    @Schema(description = "Situação do agendamento", required = true)
    @field:NotNull(message = "schedulerDTO.situation.notNull")
    var situation: EnumSchedulerSituation? = null,

    @Schema(description = "Tipo de compromisso", required = true)
    @field:NotNull(message = "schedulerDTO.compromiseType.notNull")
    var compromiseType: EnumCompromiseType? = null,

    @Schema(description = "Observação", example = "Observação sobre o agendamento", required = false)
    @field:Size(max = 4096, message = "schedulerDTO.observation.size")
    var observation: String? = null,

    @Schema(description = "Tipo de agendamento", required = true)
    @field:NotNull(message = "schedulerDTO.type.notNull")
    var type: EnumSchedulerType? = null,

    @Schema(description = "Indica se os usuários foram notificados sobre a proximidade da data do compromisso", readOnly = true)
    var notifiedAntecedence: Boolean = false
): AuditableDTO