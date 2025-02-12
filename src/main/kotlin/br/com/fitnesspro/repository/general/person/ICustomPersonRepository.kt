package br.com.fitnesspro.repository.general.person

import br.com.fitnesspro.models.general.Person
import br.com.fitnesspro.repository.common.filter.CommonImportFilter
import br.com.fitnesspro.repository.common.paging.ImportPageInfos

interface ICustomPersonRepository {

    fun getPersonsImport(filter: CommonImportFilter, pageInfos: ImportPageInfos): List<Person>
}