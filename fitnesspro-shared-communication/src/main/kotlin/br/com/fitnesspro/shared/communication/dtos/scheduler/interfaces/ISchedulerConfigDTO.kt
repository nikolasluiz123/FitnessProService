package br.com.fitnesspro.shared.communication.dtos.scheduler.interfaces

import br.com.fitnesspro.shared.communication.dtos.common.AuditableDTO

interface ISchedulerConfigDTO : AuditableDTO {
    var notification: Boolean
    var notificationAntecedenceTime: Int
    var minScheduleDensity: Int
    var maxScheduleDensity: Int
    var personId: String?
}