package br.com.fitnesspro.shared.communication.dtos.scheduler

import br.com.fitnesspro.models.scheduler.enums.EnumCompromiseType
import br.com.fitnesspro.models.scheduler.enums.EnumSchedulerSituation
import br.com.fitnesspro.models.scheduler.enums.EnumSchedulerType
import br.com.fitnesspro.shared.communication.dtos.common.AuditableDTO
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.FutureOrPresent
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

data class SchedulerDTO(
    @Schema(description = "Identificador", example = "e874d31c-0e29-4e9b-b48e-7d70d91b6a16", required = false)
    @field:Size(min = 1, max = 255, message = "O identificador deve ter entre 1 e 255 caracteres")
    override var id: String? = null,

    @Schema(description = "Data de criação", example = "2023-01-01T10:00:00", required = false, readOnly = true)
    override var creationDate: LocalDateTime? = null,

    @Schema(description = "Data de atualização", example = "2023-01-01T10:00:00", required = false, readOnly = true)
    override var updateDate: LocalDateTime? = null,

    @Schema(description = "Usuário que criou o registro", required = false)
    override var creationUserId: String? = null,

    @Schema(description = "Usuário que atualizou o registro", required = false)
    override var updateUserId: String? = null,

    @Schema(description = "Valor booleano que representa se o registro está ativo", required = true)
    @field:NotNull(message = "O campo ativo é obrigatório")
    var active: Boolean = true,

    @Schema(description = "Identificador do membro da academia", example = "e874d31c-0e29-4e9b-b48e-7d70d91b6a16", required = true)
    @field:NotNull(message = "O identificador do membro da academia é obrigatório")
    var academyMemberPersonId: String? = null,

    @Schema(description = "Identificador do profissional", example = "e874d31c-0e29-4e9b-b48e-7d70d91b6a16", required = true)
    @field:NotNull(message = "O identificador do profissional é obrigatório")
    var professionalPersonId: String? = null,

    @Schema(description = "Data agendada", example = "2023-01-01", required = true)
    @field:NotNull(message = "A data agendada é obrigatória")
    @field:FutureOrPresent(message = "Data de agendamento inválida")
    var scheduledDate: LocalDate? = null,

    @Schema(description = "Hora de início", example = "08:00", required = true)
    @field:NotNull(message = "A hora de início é obrigatória")
    var timeStart: LocalTime? = null,

    @Schema(description = "Hora de término", example = "10:00", required = true)
    @field:NotNull(message = "A hora de término é obrigatória")
    var timeEnd: LocalTime? = null,

    @Schema(description = "Data de cancelamento", example = "2023-01-01T10:00:00", required = false)
    var canceledDate: LocalDateTime? = null,

    @Schema(description = "Situação do agendamento", required = true)
    @field:NotNull(message = "A situação do agendamento é obrigatória")
    var situation: EnumSchedulerSituation? = null,

    @Schema(description = "Tipo de compromisso", required = true)
    @field:NotNull(message = "O tipo de compromisso é obrigatório")
    var compromiseType: EnumCompromiseType? = null,

    @Schema(description = "Observação", example = "Observação sobre o agendamento", required = false)
    @field:Size(max = 4096, message = "A observação deve ter no máximo 1024 caracteres")
    var observation: String? = null,

    @Schema(description = "Tipo de agendamento", required = true)
    @field:NotNull(message = "O tipo de agendamento é obrigatório")
    var type: EnumSchedulerType? = null,

    @Schema(description = "Configuração de recorrência", required = false)
    var recurrentConfig: RecurrentConfigDTO? = null
): AuditableDTO