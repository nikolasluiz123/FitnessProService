package br.com.fitnesspro.shared.communication.dtos.scheduledtask

import br.com.fitnesspro.shared.communication.dtos.common.AuditableDTO
import java.time.LocalDateTime

interface IScheduledTaskDTO: AuditableDTO {
    var active: Boolean?
    var name: String?
    var intervalMillis: Long?
    var lastExecutionTime: LocalDateTime?
    var handlerBeanName: String?
    var configJson: String?
}