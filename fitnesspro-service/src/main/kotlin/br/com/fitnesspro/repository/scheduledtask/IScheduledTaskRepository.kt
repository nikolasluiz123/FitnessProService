package br.com.fitnesspro.repository.scheduledtask

import br.com.fitnesspro.models.scheduledtask.ScheduledTask
import br.com.fitnesspro.repository.common.IFitnessProServiceRepository


interface IScheduledTaskRepository: IFitnessProServiceRepository<ScheduledTask> {

    fun findByHandlerBeanName(handlerBeanName: String): ScheduledTask?
}