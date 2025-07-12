package br.com.fitnesspro.repository.jpa.general.user

interface ICustomUserRepository {
    fun isEmailInUse(email: String, userId: String?): Boolean
}