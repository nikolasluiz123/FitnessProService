package br.com.fitnesspro.service.repository.serviceauth

import br.com.fitnesspro.service.models.serviceauth.ServiceToken
import br.com.fitnesspro.shared.communication.paging.PageInfos
import br.com.fitnesspro.shared.communication.query.filter.ServiceTokenFilter

interface ICustomServiceTokenRepository {

    fun getListServiceToken(filter: ServiceTokenFilter, pageInfos: PageInfos): List<ServiceToken>

    fun getCountListServiceToken(filter: ServiceTokenFilter): Int
}