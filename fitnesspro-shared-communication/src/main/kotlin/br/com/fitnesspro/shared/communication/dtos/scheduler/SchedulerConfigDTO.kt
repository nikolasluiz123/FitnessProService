package br.com.fitnesspro.shared.communication.dtos.scheduler

import br.com.fitnesspro.shared.communication.dtos.scheduler.interfaces.ISchedulerConfigDTO
import java.time.LocalDateTime

data class SchedulerConfigDTO(
    override var id: String? = null,
    override var creationDate: LocalDateTime? = null,
    override var updateDate: LocalDateTime? = null,
    override var notification: Boolean = false,
    override var notificationAntecedenceTime: Int = 30,
    override var minScheduleDensity: Int = 1,
    override var maxScheduleDensity: Int = 2,
    override var personId: String? = null
) : ISchedulerConfigDTO