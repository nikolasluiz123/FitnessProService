package br.com.fitnesspro.repository.general.user

import br.com.fitnesspro.models.general.User
import br.com.fitnesspro.repository.common.IFitnessProServiceRepository

interface IUserRepository: IFitnessProServiceRepository<User> {

    fun findByEmailAndPassword(email: String, password: String): User?

    fun findByEmail(email: String): User?

}