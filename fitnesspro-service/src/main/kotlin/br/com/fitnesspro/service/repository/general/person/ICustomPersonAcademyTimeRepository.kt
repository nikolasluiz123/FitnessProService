package br.com.fitnesspro.service.repository.general.person

import br.com.fitnesspro.shared.communication.query.filter.CommonImportFilter
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.dtos.general.PersonAcademyTimeDTO
import java.time.DayOfWeek
import java.time.LocalTime

interface ICustomPersonAcademyTimeRepository {

    fun getConflictPersonAcademyTime(
        personAcademyTimeId: String?,
        personId: String,
        dayOfWeek: DayOfWeek,
        start: LocalTime,
        end: LocalTime
    ): br.com.fitnesspro.service.models.general.PersonAcademyTime?

    fun getPersonAcademyTimesImport(filter: CommonImportFilter, pageInfos: ImportPageInfos): List<PersonAcademyTimeDTO>
}