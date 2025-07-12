package br.com.fitnesspro.repository.auditable.scheduledtask

import br.com.fitnesspro.models.scheduledtask.ScheduledTask
import br.com.fitnesspro.repository.common.IAuditableFitnessProRepository

interface IScheduledTaskRepository: IAuditableFitnessProRepository<ScheduledTask> {

    fun findByHandlerBeanName(handlerBeanName: String): ScheduledTask?
}