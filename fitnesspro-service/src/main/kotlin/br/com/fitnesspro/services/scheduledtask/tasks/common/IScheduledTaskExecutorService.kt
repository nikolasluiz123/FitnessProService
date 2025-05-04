package br.com.fitnesspro.services.scheduledtask.tasks.common

interface IScheduledTaskExecutorService<CONFIG> {

    fun execute(config: CONFIG?, pairIds: Pair<String, String>)
}