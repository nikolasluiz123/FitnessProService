package br.com.fitnesspro.repository.general.person

import br.com.fitnesspro.models.general.PersonAcademyTime
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
}