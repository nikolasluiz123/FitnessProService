package br.com.fitnesspro.shared.communication.dtos.general

import br.com.fitnesspro.shared.communication.dtos.common.AuditableDTO
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime

@Schema(description = "Classe DTO para manutenção dos horários de treino ou trabalho dos usuários")
data class PersonAcademyTimeDTO(
    @Schema(description = "Identificador", example = "e874d31c-0e29-4e9b-b48e-7d70d91b6a16", required = false)
    @field:Size(min = 1, max = 255, message = "baseDTO.id.size")
    override var id: String? = null,

    @Schema(description = "Data de criação", example = "2023-01-01T10:00:00", required = false, readOnly = true)
    override var creationDate: LocalDateTime? = null,

    @Schema(description = "Data de atualização", example = "2023-01-01T10:00:00", required = false, readOnly = true)
    override var updateDate: LocalDateTime? = null,

    @Schema(description = "Identificador da pessoa", example = "e874d31c-0e29-4e9b-b48e-7d70d91b6a16", required = true)
    @field:NotNull(message = "personAcademyTimeDTO.personId.notNull")
    val personId: String? = null,

    @Schema(description = "Identificador da academia", example = "e874d31c-0e29-4e9b-b48e-7d70d91b6a16", required = true)
    @field:NotNull(message = "personAcademyTimeDTO.academyId.notNull")
    val academyId: String? = null,

    @Schema(description = "Hora de início", example = "08:00", required = true)
    @field:NotNull(message = "personAcademyTimeDTO.timeStart.notNull")
    val timeStart: LocalTime? = null,

    @Schema(description = "Hora de término", example = "10:00", required = true)
    @field:NotNull(message = "personAcademyTimeDTO.timeEnd.notNull")
    val timeEnd: LocalTime? = null,

    @Schema(description = "Dia da semana", required = true)
    @field:NotNull(message = "personAcademyTimeDTO.dayOfWeek.notNull")
    val dayOfWeek: DayOfWeek? = null,

    @Schema(description = "Valor booleano que representa se o registro está ativo", required = true)
    @field:NotNull(message = "personAcademyTimeDTO.active.notNull")
    val active: Boolean = true
): AuditableDTO