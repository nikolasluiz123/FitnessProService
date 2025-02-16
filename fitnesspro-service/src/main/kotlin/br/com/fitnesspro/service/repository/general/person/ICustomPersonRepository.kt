package br.com.fitnesspro.service.repository.general.person

import br.com.fitnesspro.shared.communication.filter.CommonImportFilter
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos


interface ICustomPersonRepository {

    fun getPersonsImport(filter: CommonImportFilter, pageInfos: ImportPageInfos): List<br.com.fitnesspro.service.models.general.Person>
}