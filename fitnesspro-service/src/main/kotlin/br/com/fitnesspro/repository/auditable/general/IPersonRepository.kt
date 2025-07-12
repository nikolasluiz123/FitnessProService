package br.com.fitnesspro.repository.auditable.general

import br.com.fitnesspro.models.general.Person
import br.com.fitnesspro.repository.common.IAuditableFitnessProRepository

interface IPersonRepository: IAuditableFitnessProRepository<Person>