package br.com.fitnesspro.shared.communication.dtos.serviceauth

import br.com.fitnesspro.shared.communication.dtos.common.AuditableDTO
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

data class DeviceDTO(
    @Schema(description = "Identificador", required = true)
    @field:NotNull(message = "O identificador é obrigatório")
    override var id: String? = null,

    @Schema(description = "Modelo do dispositivo", required = true)
    @field:NotBlank(message = "O modelo é obrigatório")
    @field:Size(max = 255, message = "O modelo deve ter menos de 255 caracteres")
    val model: String? = null,

    @Schema(description = "Data de criação", example = "2023-01-01T10:00:00", required = false, readOnly = true)
    override var creationDate: LocalDateTime? = null,

    @Schema(description = "Data de atualização", example = "2023-01-01T10:00:00", required = false, readOnly = true)
    override var updateDate: LocalDateTime? = null,

    @Schema(description = "Valor booleano que representa se o registro está ativo", required = true)
    @field:NotNull(message = "O campo ativo é obrigatório")
    var active: Boolean = true
): AuditableDTO
