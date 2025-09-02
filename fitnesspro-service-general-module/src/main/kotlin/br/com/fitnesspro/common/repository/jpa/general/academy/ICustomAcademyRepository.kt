package br.com.fitnesspro.common.repository.jpa.general.academy

import br.com.fitnesspro.models.general.Academy
import br.com.fitnesspro.models.general.PersonAcademyTime
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.paging.PageInfos
import br.com.fitnesspro.shared.communication.query.filter.AcademyFilter
import br.com.fitnesspro.shared.communication.query.filter.importation.CommonImportFilter
import java.time.DayOfWeek

interface ICustomAcademyRepository {

    fun getPersonAcademyTimeList(
        personId: String,
        academyId: String? = null,
        dayOfWeek: DayOfWeek? = null
    ): List<PersonAcademyTime>

    fun getAcademiesImport(filter: CommonImportFilter, pageInfos: ImportPageInfos): List<Academy>

    fun getListAcademy(filter: AcademyFilter, pageInfos: PageInfos): List<Academy>

    fun getCountListAcademy(filter: AcademyFilter): Int
}