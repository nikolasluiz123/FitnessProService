package br.com.fitnesspro.shared.communication.dtos.workout.interfaces

import br.com.fitnesspro.shared.communication.dtos.common.AuditableDTO
import java.time.LocalDate

interface IWorkoutDTO : AuditableDTO {
    var active: Boolean
    var academyMemberPersonId: String?
    var professionalPersonId: String?
    var dateStart: LocalDate?
    var dateEnd: LocalDate?
}