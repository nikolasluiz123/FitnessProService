package br.com.fitnesspro.service.repository.general.academy

import br.com.fitnesspro.shared.communication.dtos.general.AcademyDTO
import br.com.fitnesspro.service.models.general.PersonAcademyTime
import br.com.fitnesspro.service.repository.common.filter.CommonImportFilter
import br.com.fitnesspro.service.repository.common.paging.ImportPageInfos
import java.time.DayOfWeek

interface ICustomAcademyRepository {

    fun getPersonAcademyTimeList(
        personId: String,
        academyId: String? = null,
        dayOfWeek: DayOfWeek? = null
    ): List<PersonAcademyTime>

    fun getAcademiesImport(filter: CommonImportFilter, pageInfos: ImportPageInfos): List<AcademyDTO>
}