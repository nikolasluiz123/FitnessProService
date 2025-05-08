package br.com.fitnesspro.shared.communication.dtos.workout

import br.com.fitnesspro.shared.communication.dtos.common.AuditableDTO
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDate
import java.time.LocalDateTime

@Schema(description = "Classe DTO usada para manutenção de um treino")
data class WorkoutDTO(
    @Schema(description = "Identificador", example = "e874d31c-0e29-4e9b-b48e-7d70d91b6a16", required = false)
    @field:Size(min = 1, max = 255, message = "{baseDTO.id.size}")
    override var id: String? = null,

    @Schema(description = "Data de criação", example = "2023-01-01T10:00:00", required = false, readOnly = true)
    override var creationDate: LocalDateTime? = null,

    @Schema(description = "Data de atualização", example = "2023-01-01T10:00:00", required = false, readOnly = true)
    override var updateDate: LocalDateTime? = null,

    @Schema(description = "Valor booleano que representa se o registro está ativo", required = true)
    @field:NotNull(message = "{workoutDTO.active.notNull}")
    var active: Boolean = true,

    @Schema(
        description = "Identificador do membro da academia",
        example = "e874d31c-0e29-4e9b-b48e-7d70d91b6a16",
        required = true
    )
    @field:NotNull(message = "{workoutDTO.academyMemberPersonId.notNull}")
        var academyMemberPersonId: String? = null,

    @Schema(
        description = "Identificador do profissional",
        example = "e874d31c-0e29-4e9b-b48e-7d70d91b6a16",
        required = true
    )
    @field:NotNull(message = "O identificador do profissional é obrigatório")
    var professionalPersonId: String? = null,

    @Schema(description = "Data de início", example = "2023-01-01", required = true)
    @field:NotNull(message = "{workoutDTO.dateStart.notNull}")
    var dateStart: LocalDate? = null,

    @Schema(description = "Data de término", example = "2023-01-31", required = true)
    @field:NotNull(message = "{workoutDTO.dateEnd.notNull}")
    var dateEnd: LocalDate? = null,
) : AuditableDTO