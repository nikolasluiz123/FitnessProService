package br.com.fitnesspro.repository.serviceauth

import br.com.fitnesspro.models.serviceauth.Application


interface ICustomApplicationRepository {

    fun getListApplication(): List<Application>

}