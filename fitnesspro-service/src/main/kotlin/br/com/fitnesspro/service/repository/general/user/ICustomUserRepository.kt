package br.com.fitnesspro.service.repository.general.user

interface ICustomUserRepository {
    fun isEmailInUse(email: String, userId: String?): Boolean
}