package br.com.fitnesspro.shared.communication.dtos.workout.interfaces

import br.com.fitnesspro.shared.communication.dtos.common.AuditableDTO
import java.time.DayOfWeek

interface IWorkoutGroupDTO : AuditableDTO {
    var active: Boolean
    var name: String?
    var workoutId: String?
    var dayWeek: DayOfWeek?
    var groupOrder: Int
}