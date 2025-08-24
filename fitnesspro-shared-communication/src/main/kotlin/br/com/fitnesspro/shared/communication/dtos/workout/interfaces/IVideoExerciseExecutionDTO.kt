package br.com.fitnesspro.shared.communication.dtos.workout.interfaces

import br.com.fitnesspro.shared.communication.dtos.common.AuditableDTO

interface IVideoExerciseExecutionDTO: AuditableDTO {
    var active: Boolean
    var exerciseExecutionId: String?
    var videoId: String?
}