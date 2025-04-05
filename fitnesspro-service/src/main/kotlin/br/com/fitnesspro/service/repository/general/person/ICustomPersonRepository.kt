package br.com.fitnesspro.service.repository.general.person

import br.com.fitnesspro.service.models.general.Person
import br.com.fitnesspro.service.models.general.User
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.CommonImportFilter


interface ICustomPersonRepository {

    fun getPersonsImport(filter: CommonImportFilter, pageInfos: ImportPageInfos): List<Person>

    fun getUsersImport(filter: CommonImportFilter, pageInfos: ImportPageInfos): List<User>

    fun findByEmail(email: String): Person?
}