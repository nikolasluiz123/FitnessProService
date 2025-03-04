package br.com.fitnesspro.service.repository.general.academy

import br.com.fitnesspro.service.models.general.Academy
import br.com.fitnesspro.service.models.general.PersonAcademyTime
import br.com.fitnesspro.shared.communication.dtos.general.AcademyDTO
import br.com.fitnesspro.shared.communication.filter.AcademyFilter
import br.com.fitnesspro.shared.communication.filter.CommonImportFilter
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.paging.PageInfos
import java.time.DayOfWeek

interface ICustomAcademyRepository {

    fun getPersonAcademyTimeList(
        personId: String,
        academyId: String? = null,
        dayOfWeek: DayOfWeek? = null
    ): List<PersonAcademyTime>

    fun getAcademiesImport(filter: CommonImportFilter, pageInfos: ImportPageInfos): List<AcademyDTO>

    fun getListAcademy(filter: AcademyFilter, pageInfos: PageInfos): List<Academy>

    fun getCountListAcademy(filter: AcademyFilter): Int
}