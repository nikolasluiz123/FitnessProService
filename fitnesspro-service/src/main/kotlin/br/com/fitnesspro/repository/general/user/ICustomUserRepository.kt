package br.com.fitnesspro.repository.general.user

interface ICustomUserRepository {
    fun isEmailInUse(email: String, userId: String?): Boolean
}