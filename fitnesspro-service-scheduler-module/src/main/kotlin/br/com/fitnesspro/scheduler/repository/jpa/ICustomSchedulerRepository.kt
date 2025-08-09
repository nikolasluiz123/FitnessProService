package br.com.fitnesspro.scheduler.repository.jpa

import br.com.fitnesspro.models.scheduler.Scheduler
import br.com.fitnesspro.scheduler.to.TOSchedulerAntecedenceNotificationInfo
import br.com.fitnesspro.shared.communication.enums.general.EnumUserType
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.CommonImportFilter
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