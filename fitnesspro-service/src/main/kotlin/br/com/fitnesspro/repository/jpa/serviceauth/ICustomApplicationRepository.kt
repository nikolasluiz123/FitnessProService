package br.com.fitnesspro.repository.jpa.serviceauth

import br.com.fitnesspro.models.serviceauth.Application


interface ICustomApplicationRepository {

    fun getListApplication(): List<Application>

}