package br.com.fitnesspro.authentication.repository.auditable

import br.com.fitnesspro.jpa.IAuditableFitnessProRepository
import br.com.fitnesspro.models.general.Person

interface IPersonRepository: IAuditableFitnessProRepository<Person>