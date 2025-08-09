package br.com.fitnesspro.scheduled.task.handler

interface IScheduledTaskHandler<CONFIG> {

    fun getHandlerIdentifier(): String

    fun execute(config: CONFIG?)

    fun configClass(): Class<CONFIG>?

    fun getUseHibernateTransaction() = true
}