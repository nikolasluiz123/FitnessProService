package br.com.fitnesspro.shared.communication.dtos.serviceauth

import br.com.fitnesspro.shared.communication.dtos.common.AuditableDTO
import br.com.fitnesspro.shared.communication.dtos.general.PersonDTO
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

@Schema(description = "Classe DTO usada para manutenção de um dispositivo")
data class DeviceDTO(
    @Schema(description = "Identificador", required = true)
    @field:NotNull(message = "{baseDTO.id.notNull}")
    override var id: String? = null,

    @Schema(description = "Modelo do dispositivo", required = true)
    @field:NotBlank(message = "{deviceDTO.model.notBlank}")
    @field:Size(max = 256, message = "{deviceDTO.model.size}")
    val model: String? = null,

    @Schema(description = "Marca do dispositivo", required = true)
    @field:NotBlank(message = "{deviceDTO.brand.notBlank}")
    @field:Size(max = 256, message = "{deviceDTO.brand.size}")
    val brand: String? = null,

    @Schema(description = "Versão do Android do dispositivo", required = true)
    @field:NotBlank(message = "{deviceDTO.androidVersion.notBlank}")
    @field:Size(max = 32, message = "{deviceDTO.androidVersion.size}")
    val androidVersion: String? = null,

    @Schema(description = "O token de notificação do dispositivo", required = true)
    @field:NotBlank(message = "{deviceDTO.firebaseMessagingToken.notBlank}")
    @field:Size(max = 2048, message = "{deviceDTO.firebaseMessagingToken.size}")
    val firebaseMessagingToken: String? = null,

    @Schema(description = "Identificador do timezone do dispositivo", required = true)
    @field:NotBlank(message = "{deviceDTO.zoneId.notBlank}")
    var zoneId: String? = null,

    @Schema(description = "Data de criação", example = "2023-01-01T10:00:00", required = false, readOnly = true)
    override var creationDate: LocalDateTime? = null,

    @Schema(description = "Data de atualização", example = "2023-01-01T10:00:00", required = false, readOnly = true)
    override var updateDate: LocalDateTime? = null,

    @Schema(description = "Valor booleano que representa se o registro está ativo", required = true)
    @field:NotNull(message = "{deviceDTO.active.notNull}")
    var active: Boolean = true,

    @Schema(description = "DTO da aplicação sendo utilizada no dispositivo", required = false, readOnly = true)
    var application: ApplicationDTO? = null,

    @Schema(description = "DTO da pessoa que autenticou com o dispositivo")
    var person: PersonDTO? = null
): AuditableDTO
