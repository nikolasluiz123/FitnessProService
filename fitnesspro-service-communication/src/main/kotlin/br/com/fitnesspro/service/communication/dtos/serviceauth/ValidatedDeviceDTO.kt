package br.com.fitnesspro.service.communication.dtos.serviceauth

import br.com.fitnesspro.shared.communication.dtos.general.interfaces.IPersonDTO
import br.com.fitnesspro.shared.communication.dtos.serviceauth.interfaces.IApplicationDTO
import br.com.fitnesspro.shared.communication.dtos.serviceauth.interfaces.IDeviceDTO
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

@Schema(description = "Classe DTO usada para validação de um dispositivo")
data class ValidatedDeviceDTO(
    @field:Schema(description = "Identificador", required = true)
    @field:NotNull(message = "baseDTO.id.notNull")
    override var id: String? = null,

    @field:Schema(description = "Modelo do dispositivo", required = true)
    @field:NotBlank(message = "deviceDTO.model.notBlank")
    @field:Size(max = 256, message = "deviceDTO.model.size")
    override val model: String? = null,

    @field:Schema(description = "Marca do dispositivo", required = true)
    @field:NotBlank(message = "deviceDTO.brand.notBlank")
    @field:Size(max = 256, message = "deviceDTO.brand.size")
    override val brand: String? = null,

    @field:Schema(description = "Versão do Android do dispositivo", required = true)
    @field:NotBlank(message = "deviceDTO.androidVersion.notBlank")
    @field:Size(max = 32, message = "deviceDTO.androidVersion.size")
    override val androidVersion: String? = null,

    @field:Schema(description = "O token de notificação do dispositivo", required = true)
    @field:NotBlank(message = "deviceDTO.firebaseMessagingToken.notBlank")
    @field:Size(max = 2048, message = "deviceDTO.firebaseMessagingToken.size")
    override val firebaseMessagingToken: String? = null,

    @field:Schema(description = "Identificador do timezone do dispositivo", required = true)
    @field:NotBlank(message = "deviceDTO.zoneId.notBlank")
    override var zoneId: String? = null,

    @field:Schema(description = "Data de criação", example = "2023-01-01T10:00:00", required = false, readOnly = true)
    override var creationDate: LocalDateTime? = null,

    @field:Schema(description = "Data de atualização", example = "2023-01-01T10:00:00", required = false, readOnly = true)
    override var updateDate: LocalDateTime? = null,

    @field:Schema(description = "Valor booleano que representa se o registro está ativo", required = true)
    @field:NotNull(message = "deviceDTO.active.notNull")
    override var active: Boolean = true,

    @field:Schema(description = "DTO da aplicação sendo utilizada no dispositivo", required = false, readOnly = true)
    override var application: IApplicationDTO? = null,

    @field:Schema(description = "DTO da pessoa que autenticou com o dispositivo")
    override var person: IPersonDTO? = null
) : IDeviceDTO