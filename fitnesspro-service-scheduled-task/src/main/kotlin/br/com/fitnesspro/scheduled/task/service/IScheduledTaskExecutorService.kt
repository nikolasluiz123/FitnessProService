package br.com.fitnesspro.scheduled.task.service

interface IScheduledTaskExecutorService<CONFIG> {

    fun execute(config: CONFIG?, pairIds: Pair<String, String>)
}