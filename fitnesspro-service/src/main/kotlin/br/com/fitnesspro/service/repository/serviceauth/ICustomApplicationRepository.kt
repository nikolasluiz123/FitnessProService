package br.com.fitnesspro.service.repository.serviceauth

import br.com.fitnesspro.service.models.serviceauth.Application
import br.com.fitnesspro.shared.communication.query.filter.ApplicationFilter

interface ICustomApplicationRepository {

    fun getListApplication(filter: ApplicationFilter): List<Application>

}