package br.com.fitnesspro.service.repository.general.user

import br.com.fitnesspro.core.extensions.dateTimeNow
import br.com.fitnesspro.service.models.general.User
import br.com.fitnesspro.service.repository.common.IFitnessProServiceRepository

interface IUserRepository: IFitnessProServiceRepository<User> {

    override fun <S : User?> save(entity: S & Any): S & Any {
        entity.updateDate = dateTimeNow()

        return saveAndFlush(entity)
    }

    override fun <S : User> saveAll(entities: Iterable<S>): List<S> {
        val entitiesList = entities.toList()

        entitiesList.forEach { entity ->
            entity.updateDate = dateTimeNow()
        }

        return saveAllAndFlush(entitiesList)
    }
    
    fun findByEmailAndPassword(email: String, password: String): User?

    fun findByEmail(email: String): User?

}