package br.com.fitnesspro.repository.common

import br.com.fitnesspro.models.base.BaseModel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.NoRepositoryBean

@NoRepositoryBean
interface IFitnessProServiceRepository<T: BaseModel>: JpaRepository<T, String>