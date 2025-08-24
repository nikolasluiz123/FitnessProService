package br.com.fitnesspro.shared.communication.dtos.scheduler.interfaces

import br.com.fitnesspro.shared.communication.dtos.common.AuditableDTO
import br.com.fitnesspro.shared.communication.enums.scheduler.EnumCompromiseType
import br.com.fitnesspro.shared.communication.enums.scheduler.EnumSchedulerSituation
import br.com.fitnesspro.shared.communication.enums.scheduler.EnumSchedulerType
import java.time.OffsetDateTime

interface ISchedulerDTO : AuditableDTO {
    var active: Boolean
    var academyMemberPersonId: String?
    var professionalPersonId: String?
    var dateTimeStart: OffsetDateTime?
    var dateTimeEnd: OffsetDateTime?
    var canceledDate: OffsetDateTime?
    var cancellationPersonId: String?
    var situation: EnumSchedulerSituation?
    var compromiseType: EnumCompromiseType?
    var observation: String?
    var type: EnumSchedulerType?
    var notifiedAntecedence: Boolean
}