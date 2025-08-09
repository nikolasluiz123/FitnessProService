package br.com.fitnesspro.authentication.repository.jpa

interface ICustomUserRepository {
    fun isEmailInUse(email: String, userId: String?): Boolean
}