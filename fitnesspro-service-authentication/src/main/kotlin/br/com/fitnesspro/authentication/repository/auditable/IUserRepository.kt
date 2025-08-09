package br.com.fitnesspro.authentication.repository.auditable

import br.com.fitnesspro.jpa.IAuditableFitnessProRepository
import br.com.fitnesspro.models.general.User

interface IUserRepository: IAuditableFitnessProRepository<User> {

    fun findByEmailAndPassword(email: String, password: String): User?

    fun findByEmail(email: String): User?

}