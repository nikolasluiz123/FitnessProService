package br.com.fitnesspro.repository.general.person

import br.com.fitnesspro.models.general.Person
import br.com.fitnesspro.models.general.User
import br.com.fitnesspro.shared.communication.dtos.general.PersonDTO
import br.com.fitnesspro.shared.communication.paging.CommonPageInfos
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.CommonImportFilter
import br.com.fitnesspro.shared.communication.query.filter.PersonFilter


interface ICustomPersonRepository {

    fun getPersonsImport(filter: CommonImportFilter, pageInfos: ImportPageInfos): List<Person>

    fun getUsersImport(filter: CommonImportFilter, pageInfos: ImportPageInfos): List<User>

    fun findByEmail(email: String, password: String?): Person?

    fun getListPersons(filter: PersonFilter, pageInfos: CommonPageInfos): List<PersonDTO>

    fun getCountListPersons(filter: PersonFilter): Int
}