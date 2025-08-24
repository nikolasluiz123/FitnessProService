package br.com.fitnesspro.shared.communication.dtos.general.interfaces

import br.com.fitnesspro.shared.communication.dtos.common.AuditableDTO
import br.com.fitnesspro.shared.communication.enums.report.EnumReportContext

interface ISchedulerReportDTO: AuditableDTO {
    var personId: String?
    var reportId: String?
    var reportContext: EnumReportContext?
    var active: Boolean
}