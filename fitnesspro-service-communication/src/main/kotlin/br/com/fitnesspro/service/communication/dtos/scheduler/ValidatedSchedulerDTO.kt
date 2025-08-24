package br.com.fitnesspro.service.communication.dtos.scheduler

import br.com.fitnesspro.shared.communication.dtos.scheduler.interfaces.ISchedulerDTO
import br.com.fitnesspro.shared.communication.enums.scheduler.EnumCompromiseType
import br.com.fitnesspro.shared.communication.enums.scheduler.EnumSchedulerSituation
import br.com.fitnesspro.shared.communication.enums.scheduler.EnumSchedulerType
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDateTime
import java.time.OffsetDateTime

@Schema(description = "Classe DTO usada para manutenção de um agendamento")
data class ValidatedSchedulerDTO(
    @field:Schema(description = "Identificador", example = "e874d31c-0e29-4e9b-b48e-7d70d91b6a16", required = false)
    @field:Size(min = 1, max = 255, message = "baseDTO.id.size")
    override var id: String? = null,

    @field:Schema(description = "Data de criação", example = "2023-01-01T10:00:00", required = false, readOnly = true)
    override var creationDate: LocalDateTime? = null,

    @field:Schema(description = "Data de atualização", example = "2023-01-01T10:00:00", required = false, readOnly = true)
    override var updateDate: LocalDateTime? = null,

    @field:Schema(description = "Valor booleano que representa se o registro está ativo", required = true)
    @field:NotNull(message = "schedulerDTO.active.notNull")
    override var active: Boolean = true,

    @field:Schema(description = "Identificador do membro da academia", example = "e874d31c-0e29-4e9b-b48e-7d70d91b6a16", required = true)
    @field:NotNull(message = "schedulerDTO.academyMemberPersonId.notNull")
    override var academyMemberPersonId: String? = null,

    @field:Schema(description = "Identificador do profissional", example = "e874d31c-0e29-4e9b-b48e-7d70d91b6a16", required = true)
    @field:NotNull(message = "schedulerDTO.professionalPersonId.notNull")
    override var professionalPersonId: String? = null,

    @field:Schema(description = "Hora de início", example = "2025-05-10T10:15:30-03:00", required = true)
    @field:NotNull(message = "schedulerDTO.timeStart.notNull")
    override var dateTimeStart: OffsetDateTime? = null,

    @field:Schema(description = "Hora de término", example = "2025-05-10T10:15:30-03:00", required = true)
    @field:NotNull(message = "schedulerDTO.timeEnd.notNull")
    override var dateTimeEnd: OffsetDateTime? = null,

    @field:Schema(description = "Data de cancelamento", example = "2025-05-10T10:15:30-03:00", required = false)
    override var canceledDate: OffsetDateTime? = null,

    @field:Schema(description = "Identificador da pessoa que cancelou o agendamento", example = "e874d31c-0e29-4e9b-b48e-7d70d9", required = false)
    override var cancellationPersonId: String? = null,

    @field:Schema(description = "Situação do agendamento", required = true)
    @field:NotNull(message = "schedulerDTO.situation.notNull")
    override var situation: EnumSchedulerSituation? = null,

    @field:Schema(description = "Tipo de compromisso", required = true)
    @field:NotNull(message = "schedulerDTO.compromiseType.notNull")
    override var compromiseType: EnumCompromiseType? = null,

    @field:Schema(description = "Observação", example = "Observação sobre o agendamento", required = false)
    @field:Size(max = 4096, message = "schedulerDTO.observation.size")
    override var observation: String? = null,

    @field:Schema(description = "Tipo de agendamento", required = true)
    @field:NotNull(message = "schedulerDTO.type.notNull")
    override var type: EnumSchedulerType? = null,

    @field:Schema(description = "Indica se os usuários foram notificados sobre a proximidade da data do compromisso", readOnly = true)
    override var notifiedAntecedence: Boolean = false
) : ISchedulerDTO