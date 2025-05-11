package br.com.fitnesspro.repository.scheduler

import br.com.fitnesspro.models.scheduler.Scheduler
import br.com.fitnesspro.shared.communication.enums.general.EnumUserType
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.CommonImportFilter
import br.com.fitnesspro.to.TOSchedulerAntecedenceNotificationInfo
import java.time.OffsetDateTime

interface ICustomSchedulerRepository {

    fun getHasSchedulerConflict(
        schedulerId: String?,
        personId: String,
        userType: EnumUserType,
        start: OffsetDateTime,
        end: OffsetDateTime
    ): Boolean

    fun getSchedulesImport(filter: CommonImportFilter, pageInfos: ImportPageInfos): List<Scheduler>

    fun getListTOSchedulerAntecedenceNotificationInfo(): List<TOSchedulerAntecedenceNotificationInfo>
}