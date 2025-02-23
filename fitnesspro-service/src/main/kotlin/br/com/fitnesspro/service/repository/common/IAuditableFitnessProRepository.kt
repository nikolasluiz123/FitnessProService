package br.com.fitnesspro.service.repository.common

import br.com.fitnesspro.core.extensions.dateTimeNow
import br.com.fitnesspro.service.models.base.AuditableModel
import org.springframework.data.repository.NoRepositoryBean

@NoRepositoryBean
interface IAuditableFitnessProRepository<T: AuditableModel>:
    IFitnessProServiceRepository<T> {

    override fun <S : T> save(entity: S): S {
        entity.updateDate = dateTimeNow()

        return saveAndFlush(entity)
    }

    override fun <S : T> saveAll(entities: Iterable<S>): List<S> {
        val entitiesList = entities.toList()

        entitiesList.forEach { entity ->
            entity.updateDate = dateTimeNow()
        }

        return saveAllAndFlush(entitiesList)
    }
}