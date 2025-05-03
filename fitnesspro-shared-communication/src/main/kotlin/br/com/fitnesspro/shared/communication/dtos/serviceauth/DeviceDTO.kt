package br.com.fitnesspro.shared.communication.dtos.serviceauth

import br.com.fitnesspro.shared.communication.dtos.common.AuditableDTO
import br.com.fitnesspro.shared.communication.dtos.general.PersonDTO
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
    @field:Size(max = 256, message = "O modelo deve ter menos de 256 caracteres")
    val model: String? = null,

    @Schema(description = "Marca do dispositivo", required = true)
    @field:NotBlank(message = "A marca é obrigatória")
    @field:Size(max = 256, message = "A marca deve ter menos de 256 caracteres")
    val brand: String? = null,

    @Schema(description = "Versão do Android do dispositivo", required = true)
    @field:NotBlank(message = "A versão do Android é obrigatória")
    @field:Size(max = 32, message = "A versão do Android deve ter menos de 32 caracteres")
    val androidVersion: String? = null,

    @Schema(description = "O token de notificação do dispositivo", required = true)
    @field:NotBlank(message = "O token de notificação é obrigatório")
    @field:Size(max = 2048, message = "O token de notificação deve ter menos de 2048 caracteres")
    val firebaseMessagingToken: String? = null,

    @Schema(description = "Identificador do timezone do dispositivo", required = true)
    @field:NotBlank(message = "O timezone é obrigatório")
    var zoneId: String? = null,

    @Schema(description = "Data de criação", example = "2023-01-01T10:00:00", required = false, readOnly = true)
    override var creationDate: LocalDateTime? = null,

    @Schema(description = "Data de atualização", example = "2023-01-01T10:00:00", required = false, readOnly = true)
    override var updateDate: LocalDateTime? = null,

    @Schema(description = "Valor booleano que representa se o registro está ativo", required = true)
    @field:NotNull(message = "O campo ativo é obrigatório")
    var active: Boolean = true,

    @Schema(description = "DTO da aplicação sendo utilizada no dispositivo", required = false, readOnly = true)
    var application: ApplicationDTO? = null,

    @Schema(description = "DTO da pessoa que autenticou com o dispositivo")
    var person: PersonDTO? = null
): AuditableDTO
