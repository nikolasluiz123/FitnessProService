package br.com.fitnesspro.authentication.repository.jpa

import br.com.fitnesspro.models.serviceauth.Application


interface ICustomApplicationRepository {

    fun getListApplication(): List<Application>

}