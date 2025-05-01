package br.com.fitnesspro.manager

interface IScheduledTaskHandler<CONFIG> {

    fun getHandlerIdentifier(): String

    fun execute(config: CONFIG)

    fun configClass(): Class<CONFIG>

    fun getUseHibernateTransaction() = true
}