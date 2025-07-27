package br.com.fitnesspro.shared.communication.dtos.workout

import br.com.fitnesspro.shared.communication.dtos.common.AuditableDTO
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

@Schema(description = "Classe DTO usada para manutenção dos grupos musculares predefinidos")
data class WorkoutGroupPreDefinitionDTO(
    @field:Schema(description = "Identificador", example = "e874d31c-0e29-4e9b-b48e-7d70d91b6a16", required = false)
    @field:Size(min = 1, max = 255, message = "baseDTO.id.size")
    override var id: String? = null,

    @field:Schema(description = "Data de criação", example = "2023-01-01T10:00:00", required = false, readOnly = true)
    override var creationDate: LocalDateTime? = null,

    @field:Schema(description = "Data de atualização", example = "2023-01-01T10:00:00", required = false, readOnly = true)
    override var updateDate: LocalDateTime? = null,

    @field:Schema(description = "Valor booleano que representa se o registro está ativo", required = true)
    @field:NotNull(message = "workoutGroupPreDefinitionDTO.active.notNull")
    var active: Boolean = true,

    @field:Schema(description = "Nome do grupo de treino", example = "Treino A", required = true)
    @field:NotEmpty(message = "workoutGroupPreDefinitionDTO.name.notBlank") // Sugestão: Adicionar esta chave ao ValidationMessages.properties
    @field:Size(max = 255, message = "workoutGroupPreDefinitionDTO.name.size")
    var name: String? = null,

    @field:Schema(description = "Identificador do profissional", required = true)
    @field:NotEmpty(message = "workoutGroupPreDefinitionDTO.personalTrainerPersonId.notNull")
    @field:Size(min = 1, max = 255, message = "baseDTO.id.size")
    var personalTrainerPersonId: String? = null
): AuditableDTO
