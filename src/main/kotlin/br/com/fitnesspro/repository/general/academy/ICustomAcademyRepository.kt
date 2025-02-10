package br.com.fitnesspro.repository.general.academy

import br.com.fitnesspro.models.general.PersonAcademyTime
import java.time.DayOfWeek

interface ICustomAcademyRepository {

    fun getPersonAcademyTimeList(
        personId: String,
        academyId: String? = null,
        dayOfWeek: DayOfWeek? = null
    ): List<PersonAcademyTime>
}