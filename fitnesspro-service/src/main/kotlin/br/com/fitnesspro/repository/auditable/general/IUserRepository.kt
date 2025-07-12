package br.com.fitnesspro.repository.auditable.general

import br.com.fitnesspro.models.general.User
import br.com.fitnesspro.repository.common.IAuditableFitnessProRepository

interface IUserRepository: IAuditableFitnessProRepository<User> {

    fun findByEmailAndPassword(email: String, password: String): User?

    fun findByEmail(email: String): User?

}