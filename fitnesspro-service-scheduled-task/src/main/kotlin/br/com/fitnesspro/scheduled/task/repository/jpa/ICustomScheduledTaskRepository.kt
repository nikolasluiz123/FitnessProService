package br.com.fitnesspro.scheduled.task.repository.jpa

import br.com.fitnesspro.models.scheduledtask.ScheduledTask

interface ICustomScheduledTaskRepository {

    fun getListScheduledTask(): List<ScheduledTask>
}