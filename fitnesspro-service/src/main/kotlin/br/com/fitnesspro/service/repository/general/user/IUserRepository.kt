package br.com.fitnesspro.service.repository.general.user

import br.com.fitnesspro.service.models.general.User
import br.com.fitnesspro.service.repository.common.IAuditableFitnessProRepository


interface IUserRepository: IAuditableFitnessProRepository<User> {

    fun findByEmailAndPassword(email: String, password: String): User?

    fun findByEmail(email: String): User?

}