package br.com.fitnesspro.shared.communication.dtos.workout.interfaces.health

import br.com.fitnesspro.shared.communication.dtos.common.AuditableDTO
import br.com.fitnesspro.shared.communication.enums.health.EnumSleepStage
import java.time.Instant

interface IHealthConnectSleepStagesDTO : AuditableDTO {
    var active: Boolean
    var healthConnectSleepSessionId: String?
    var startTime: Instant?
    var endTime: Instant?
    var stage: EnumSleepStage?
}