package br.com.fitnesspro.shared.communication.dtos.workout.interfaces.health

import br.com.fitnesspro.shared.communication.dtos.common.AuditableDTO
import java.time.Instant
import java.time.ZoneOffset

interface IHealthConnectCaloriesBurnedDTO : AuditableDTO {
    var active: Boolean
    var healthConnectMetadataId: String?
    var exerciseExecutionId: String?
    var caloriesInKcal: Long?
    var startTime: Instant?
    var endTime: Instant?
    var startZoneOffset: ZoneOffset?
    var endZoneOffset: ZoneOffset?
}