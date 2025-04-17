package br.com.fitnesspro.service.repository.serviceauth

import br.com.fitnesspro.service.models.serviceauth.Application

interface ICustomApplicationRepository {

    fun getListApplication(): List<Application>

}