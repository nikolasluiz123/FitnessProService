package br.com.fitnesspro.shared.communication.dtos.scheduler

import br.com.fitnesspro.shared.communication.dtos.scheduler.interfaces.ISchedulerDTO
import br.com.fitnesspro.shared.communication.enums.scheduler.EnumCompromiseType
import br.com.fitnesspro.shared.communication.enums.scheduler.EnumSchedulerSituation
import br.com.fitnesspro.shared.communication.enums.scheduler.EnumSchedulerType
import java.time.LocalDateTime
import java.time.OffsetDateTime

data class SchedulerDTO(
    override var id: String? = null,
    override var creationDate: LocalDateTime? = null,
    override var updateDate: LocalDateTime? = null,
    override var active: Boolean = true,
    override var academyMemberPersonId: String? = null,
    override var professionalPersonId: String? = null,
    override var dateTimeStart: OffsetDateTime? = null,
    override var dateTimeEnd: OffsetDateTime? = null,
    override var canceledDate: OffsetDateTime? = null,
    override var cancellationPersonId: String? = null,
    override var situation: EnumSchedulerSituation? = null,
    override var compromiseType: EnumCompromiseType? = null,
    override var observation: String? = null,
    override var type: EnumSchedulerType? = null,
    override var notifiedAntecedence: Boolean = false
) : ISchedulerDTO