package br.com.fitnesspro.authentication.repository.jpa

import br.com.fitnesspro.models.general.PersonAcademyTime
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.CommonImportFilter
import java.time.DayOfWeek
import java.time.LocalTime

interface ICustomPersonAcademyTimeRepository {

    fun getConflictPersonAcademyTime(
        personAcademyTimeId: String?,
        personId: String,
        dayOfWeek: DayOfWeek,
        start: LocalTime,
        end: LocalTime
    ): PersonAcademyTime?

    fun getPersonAcademyTimesImport(
        filter: CommonImportFilter,
        pageInfos: ImportPageInfos
    ): List<PersonAcademyTime>
}