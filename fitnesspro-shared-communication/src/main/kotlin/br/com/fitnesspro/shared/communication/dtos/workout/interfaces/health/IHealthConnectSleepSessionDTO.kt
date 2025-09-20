package br.com.fitnesspro.shared.communication.dtos.workout.interfaces.health

import br.com.fitnesspro.shared.communication.dtos.common.AuditableDTO
import java.time.Instant

interface IHealthConnectSleepSessionDTO : AuditableDTO {
    var active: Boolean
    var healthConnectMetadataId: String?
    var startTime: Instant?
    var endTime: Instant?
    var title: String?
    var notes: String?
}