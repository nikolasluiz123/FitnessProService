package br.com.fitnesspro.service.repository.scheduler

import br.com.fitnesspro.models.general.enums.EnumUserType
import br.com.fitnesspro.shared.communication.query.filter.CommonImportFilter
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.dtos.scheduler.SchedulerDTO
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