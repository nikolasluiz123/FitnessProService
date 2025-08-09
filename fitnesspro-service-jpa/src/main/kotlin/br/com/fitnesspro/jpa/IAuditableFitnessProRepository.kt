package br.com.fitnesspro.jpa

import br.com.fitnesspro.models.base.AuditableModel
import org.springframework.data.repository.NoRepositoryBean

@NoRepositoryBean
interface IAuditableFitnessProRepository<T: AuditableModel>: IFitnessProServiceRepository<T>