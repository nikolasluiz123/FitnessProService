package br.com.fitnesspro.shared.communication.dtos.workout.interfaces.health

import br.com.fitnesspro.shared.communication.dtos.common.AuditableDTO
import java.time.Instant

interface IHealthConnectHeartRateSamplesDTO : AuditableDTO {
    var active: Boolean
    var healthConnectHeartRateId: String?
    var sampleTime: Instant?
    var bpm: Long?
}