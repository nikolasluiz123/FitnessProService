package br.com.fitnesspro.shared.communication.dtos.scheduledtask

import java.time.LocalDateTime

data class ScheduledTaskDTO(
    override var id: String? = null,
    override var creationDate: LocalDateTime? = null,
    override var updateDate: LocalDateTime? = null,
    override var active: Boolean? = null,
    override var name: String? = null,
    override var intervalMillis: Long? = null,
    override var lastExecutionTime: LocalDateTime? = null,
    override var handlerBeanName: String? = null,
    override var configJson: String? = null,
) : IScheduledTaskDTO