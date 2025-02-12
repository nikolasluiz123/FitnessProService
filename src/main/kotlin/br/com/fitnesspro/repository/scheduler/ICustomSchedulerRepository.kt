package br.com.fitnesspro.repository.scheduler

import br.com.fitnesspro.dto.scheduler.SchedulerDTO
import br.com.fitnesspro.models.general.enums.EnumUserType
import br.com.fitnesspro.repository.common.filter.CommonImportFilter
import br.com.fitnesspro.repository.common.paging.ImportPageInfos
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

    fun getSchedulesImport(filter: CommonImportFilter, pageInfos: ImportPageInfos): List<SchedulerDTO>
}