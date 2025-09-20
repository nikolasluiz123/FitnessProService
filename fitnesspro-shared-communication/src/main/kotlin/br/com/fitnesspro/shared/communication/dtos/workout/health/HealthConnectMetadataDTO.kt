package br.com.fitnesspro.shared.communication.dtos.workout.health

import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.health.IHealthConnectMetadataDTO
import br.com.fitnesspro.shared.communication.enums.health.EnumRecordingMethod
import java.time.LocalDateTime

data class HealthConnectMetadataDTO(
    override var id: String? = null,
    override var creationDate: LocalDateTime? = null,
    override var updateDate: LocalDateTime? = null,
    override var active: Boolean = true,
    override var dataOriginPackage: String? = null,
    override var lastModifiedTime: LocalDateTime? = null,
    override var clientRecordId: String? = null,
    override var deviceManufacturer: String? = null,
    override var deviceModel: String? = null,
    override var recordingMethod: EnumRecordingMethod? = null
) : IHealthConnectMetadataDTO