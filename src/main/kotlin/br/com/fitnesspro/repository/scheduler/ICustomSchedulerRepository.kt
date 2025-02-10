package br.com.fitnesspro.repository.scheduler

import br.com.fitnesspro.models.general.enums.EnumUserType
import java.time.LocalDate
import java.time.LocalTime

interface ICustomSchedulerRepository {

    fun getHasSchedulerConflict(
        schedulerId: String?,
        personId: String,
        userType: EnumUserType,
        scheduledDate: LocalDate,
        start: LocalTime,
        end: LocalTime
    ): Boolean
}