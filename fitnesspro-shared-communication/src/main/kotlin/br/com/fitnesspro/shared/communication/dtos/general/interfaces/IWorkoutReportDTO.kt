package br.com.fitnesspro.shared.communication.dtos.general.interfaces

import br.com.fitnesspro.shared.communication.dtos.common.AuditableDTO
import br.com.fitnesspro.shared.communication.enums.report.EnumReportContext

interface IWorkoutReportDTO: AuditableDTO {
    var workoutId: String?
    var reportId: String?
    var personId: String?
    var reportContext: EnumReportContext?
    var active: Boolean
}