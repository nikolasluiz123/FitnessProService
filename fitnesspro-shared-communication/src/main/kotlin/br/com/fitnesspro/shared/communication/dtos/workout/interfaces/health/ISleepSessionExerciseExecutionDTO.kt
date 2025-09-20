package br.com.fitnesspro.shared.communication.dtos.workout.interfaces.health

import br.com.fitnesspro.shared.communication.dtos.common.AuditableDTO

interface ISleepSessionExerciseExecutionDTO : AuditableDTO {
    var active: Boolean
    var healthConnectSleepSessionId: String?
    var exerciseExecutionId: String?
}