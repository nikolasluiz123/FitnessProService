package br.com.fitnesspro.repository.general.person

import br.com.fitnesspro.dto.general.PersonAcademyTimeDTO
import br.com.fitnesspro.models.general.PersonAcademyTime
import br.com.fitnesspro.repository.common.filter.CommonImportFilter
import br.com.fitnesspro.repository.common.paging.ImportPageInfos
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

    fun getPersonAcademyTimesImport(filter: CommonImportFilter, pageInfos: ImportPageInfos): List<PersonAcademyTimeDTO>
}