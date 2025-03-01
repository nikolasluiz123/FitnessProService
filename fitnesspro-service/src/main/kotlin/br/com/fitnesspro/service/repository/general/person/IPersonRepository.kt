package br.com.fitnesspro.service.repository.general.person

import br.com.fitnesspro.core.extensions.dateTimeNow
import br.com.fitnesspro.service.models.general.Person
import br.com.fitnesspro.service.repository.common.IFitnessProServiceRepository


interface IPersonRepository: IFitnessProServiceRepository<Person> {

    override fun <S : Person?> save(entity: S & Any): S & Any {
        entity.updateDate = dateTimeNow()

        return saveAndFlush(entity)
    }

    override fun <S : Person> saveAll(entities: Iterable<S>): List<S> {
        val entitiesList = entities.toList()

        entitiesList.forEach { entity ->
            entity.updateDate = dateTimeNow()
        }

        return saveAllAndFlush(entitiesList)
    }
}