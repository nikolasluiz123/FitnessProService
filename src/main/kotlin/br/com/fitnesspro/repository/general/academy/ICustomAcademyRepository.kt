package br.com.fitnesspro.repository.general.academy

import br.com.fitnesspro.dto.general.AcademyDTO
import br.com.fitnesspro.models.general.PersonAcademyTime
import br.com.fitnesspro.repository.common.filter.CommonImportFilter
import br.com.fitnesspro.repository.common.paging.ImportPageInfos
import java.time.DayOfWeek

interface ICustomAcademyRepository {

    fun getPersonAcademyTimeList(
        personId: String,
        academyId: String? = null,
        dayOfWeek: DayOfWeek? = null
    ): List<PersonAcademyTime>

    fun getAcademiesImport(filter: CommonImportFilter, pageInfos: ImportPageInfos): List<AcademyDTO>
}