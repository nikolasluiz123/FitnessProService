package br.com.fitnesspro.service.communication.dtos.general

import br.com.fitnesspro.shared.communication.dtos.common.AuditableDTO
import br.com.fitnesspro.shared.communication.dtos.general.interfaces.IPersonAcademyTimeDTO
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime

@Schema(description = "Classe DTO para manutenção dos horários de treino ou trabalho dos usuários")
data class ValidatedPersonAcademyTimeDTO(
    @field:Schema(description = "Identificador", example = "e874d31c-0e29-4e9b-b48e-7d70d91b6a16", required = false)
    @field:Size(min = 1, max = 255, message = "baseDTO.id.size")
    override var id: String? = null,

    @field:Schema(description = "Data de criação", example = "2023-01-01T10:00:00", required = false, readOnly = true)
    override var creationDate: LocalDateTime? = null,

    @field:Schema(description = "Data de atualização", example = "2023-01-01T10:00:00", required = false, readOnly = true)
    override var updateDate: LocalDateTime? = null,

    @field:Schema(description = "Identificador da pessoa", example = "e874d31c-0e29-4e9b-b48e-7d70d91b6a16", required = true)
    @field:NotNull(message = "personAcademyTimeDTO.personId.notNull")
    override val personId: String? = null,

    @field:Schema(description = "Identificador da academia", example = "e874d31c-0e29-4e9b-b48e-7d70d91b6a16", required = true)
    @field:NotNull(message = "personAcademyTimeDTO.academyId.notNull")
    override val academyId: String? = null,

    @field:Schema(description = "Hora de início", example = "08:00", required = true)
    @field:NotNull(message = "personAcademyTimeDTO.timeStart.notNull")
    override val timeStart: LocalTime? = null,

    @field:Schema(description = "Hora de término", example = "10:00", required = true)
    @field:NotNull(message = "personAcademyTimeDTO.timeEnd.notNull")
    override val timeEnd: LocalTime? = null,

    @field:Schema(description = "Dia da semana", required = true)
    @field:NotNull(message = "personAcademyTimeDTO.dayOfWeek.notNull")
    override val dayOfWeek: DayOfWeek? = null,

    @field:Schema(description = "Valor booleano que representa se o registro está ativo", required = true)
    @field:NotNull(message = "personAcademyTimeDTO.active.notNull")
    override val active: Boolean = true
): IPersonAcademyTimeDTO