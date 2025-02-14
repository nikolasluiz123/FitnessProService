package br.com.fitnesspro.service.repository.general.user

import br.com.fitnesspro.service.models.general.User
import br.com.fitnesspro.service.repository.common.IAuditableFitnessProRepository

interface IUserRepository:
    br.com.fitnesspro.service.repository.common.IAuditableFitnessProRepository<br.com.fitnesspro.service.models.general.User> {

    fun findByEmailAndPassword(email: String, password: String): br.com.fitnesspro.service.models.general.User?

    fun findByEmail(email: String): br.com.fitnesspro.service.models.general.User?

}