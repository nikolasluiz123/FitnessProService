package br.com.fitnesspro.jpa

import br.com.fitnesspro.core.extensions.dateTimeNow
import br.com.fitnesspro.models.base.AuditableModel
import jakarta.persistence.EntityManager
import org.springframework.data.jpa.repository.support.JpaEntityInformation
import org.springframework.data.jpa.repository.support.SimpleJpaRepository

class AuditableRepositoryImpl<T : AuditableModel>(
    entityInformation: JpaEntityInformation<T, *>,
    entityManager: EntityManager
) : SimpleJpaRepository<T, String>(entityInformation, entityManager) {

    override fun <S : T> save(entity: S): S {
        entity.updateDate = dateTimeNow()
        return super.save(entity)
    }

    override fun <S : T> saveAll(entities: Iterable<S>): List<S> {
        entities.forEach { it.updateDate = dateTimeNow() }
        return super.saveAll(entities)
    }
}