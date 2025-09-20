package br.com.fitnesspro.service.communication.dtos.workout.health

import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.health.IHealthConnectMetadataDTO
import br.com.fitnesspro.shared.communication.enums.health.EnumRecordingMethod
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.PastOrPresent
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

@Schema(description = "DTO para metadados do Health Connect")
data class ValidatedHealthConnectMetadataDTO(
    @field:Schema(description = "Identificador (originado do Health Connect)", required = true)
    @field:NotNull(message = "healthConnectMetadata.id.notNull")
    @field:Size(min = 1, max = 255, message = "baseDTO.id.size")
    override var id: String? = null,

    @field:Schema(description = "Data de criação", required = false, readOnly = true)
    override var creationDate: LocalDateTime? = null,

    @field:Schema(description = "Data de atualização", required = false, readOnly = true)
    override var updateDate: LocalDateTime? = null,

    @field:Schema(description = "Registro ativo", required = true)
    @field:NotNull(message = "healthConnectMetadata.active.notNull")
    override var active: Boolean = true,

    @field:Schema(description = "Pacote de origem do dado", required = false)
    @field:Size(max = 255, message = "healthConnectMetadata.dataOriginPackage.size")
    override var dataOriginPackage: String? = null,

    @field:Schema(description = "Data da última modificação", required = false)
    @field:PastOrPresent(message = "healthConnectMetadata.lastModifiedTime.pastOrPresent")
    override var lastModifiedTime: LocalDateTime? = null,

    @field:Schema(description = "ID do registro no cliente de origem", required = false)
    @field:Size(max = 255, message = "healthConnectMetadata.clientRecordId.size")
    override var clientRecordId: String? = null,

    @field:Schema(description = "Fabricante do dispositivo", required = false)
    @field:Size(max = 255, message = "healthConnectMetadata.deviceManufacturer.size")
    override var deviceManufacturer: String? = null,

    @field:Schema(description = "Modelo do dispositivo", required = false)
    @field:Size(max = 255, message = "healthConnectMetadata.deviceModel.size")
    override var deviceModel: String? = null,

    @field:Schema(description = "Método de gravação (ATIVO ou PASSIVO)", required = false)
    override var recordingMethod: EnumRecordingMethod? = null
) : IHealthConnectMetadataDTO