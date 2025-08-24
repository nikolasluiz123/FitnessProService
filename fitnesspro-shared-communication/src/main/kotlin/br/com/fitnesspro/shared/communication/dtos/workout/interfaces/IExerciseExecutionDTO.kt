package br.com.fitnesspro.shared.communication.dtos.workout.interfaces

import br.com.fitnesspro.shared.communication.dtos.common.AuditableDTO
import java.time.LocalDateTime

interface IExerciseExecutionDTO : AuditableDTO {
    var active: Boolean
    var duration: Long?
    var repetitions: Int?
    var set: Int?
    var rest: Long?
    var weight: Double?
    var date: LocalDateTime?
    var exerciseId: String?
}