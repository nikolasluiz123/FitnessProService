package br.com.fitnesspro.scheduled.task.repository.auditable

import br.com.fitnesspro.jpa.IAuditableFitnessProRepository
import br.com.fitnesspro.models.scheduledtask.ScheduledTask

interface IScheduledTaskRepository: IAuditableFitnessProRepository<ScheduledTask> {

    fun findByHandlerBeanName(handlerBeanName: String): ScheduledTask?
}