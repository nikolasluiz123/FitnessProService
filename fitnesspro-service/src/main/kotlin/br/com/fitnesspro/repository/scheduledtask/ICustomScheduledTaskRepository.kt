package br.com.fitnesspro.repository.scheduledtask

import br.com.fitnesspro.models.scheduledtask.ScheduledTask

interface ICustomScheduledTaskRepository {

    fun getListScheduledTask(): List<ScheduledTask>
}