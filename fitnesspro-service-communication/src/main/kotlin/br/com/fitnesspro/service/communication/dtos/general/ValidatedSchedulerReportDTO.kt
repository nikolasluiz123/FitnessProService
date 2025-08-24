package br.com.fitnesspro.service.communication.dtos.general

import br.com.fitnesspro.shared.communication.dtos.general.interfaces.ISchedulerReportDTO
import br.com.fitnesspro.shared.communication.enums.report.EnumReportContext
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

@Schema(description = "Classe DTO usada para vincular o relatório de agendamentos ao profissional")
data class ValidatedSchedulerReportDTO(
    @field:Schema(description = "Identificador", example = "e874d31c-0e29-4e9b-b48e-7d70d91b6a16", required = false)
    @field:Size(min = 1, max = 255, message = "baseDTO.id.size")
    override var id: String? = null,

    @field:Schema(description = "Data de criação", example = "2023-01-01T10:00:00", required = false, readOnly = true)
    override var creationDate: LocalDateTime? = null,

    @field:Schema(description = "Data de atualização", example = "2023-01-01T10:00:00", required = false, readOnly = true)
    override var updateDate: LocalDateTime? = null,

    @field:Schema(description = "Identificador do profissional", required = true)
    @field:NotNull(message = "schedulerReportDTO.personId.notNull")
    override var personId: String? = null,

    @field:Schema(description = "Identificador do relatório", required = true)
    @field:NotNull(message = "schedulerReportDTO.reportId.notNull")
    override var reportId: String? = null,

    @field:Schema(description = "Contexto do relatório", required = true)
    @field:NotNull(message = "schedulerReportDTO.reportContext.notNull")
    override var reportContext: EnumReportContext? = null,

    @field:Schema(description = "Valor booleano que representa se o registro está ativo", required = true)
    @field:NotNull(message = "schedulerReportDTO.active.notNull")
    override var active: Boolean = true,
): ISchedulerReportDTO