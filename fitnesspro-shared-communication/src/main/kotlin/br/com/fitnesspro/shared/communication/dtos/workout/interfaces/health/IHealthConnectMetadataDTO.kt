package br.com.fitnesspro.shared.communication.dtos.workout.interfaces.health

import br.com.fitnesspro.shared.communication.dtos.common.AuditableDTO
import br.com.fitnesspro.shared.communication.enums.health.EnumRecordingMethod
import java.time.LocalDateTime

interface IHealthConnectMetadataDTO : AuditableDTO {
    var active: Boolean
    var dataOriginPackage: String?
    var lastModifiedTime: LocalDateTime?
    var clientRecordId: String?
    var deviceManufacturer: String?
    var deviceModel: String?
    var recordingMethod: EnumRecordingMethod?
}