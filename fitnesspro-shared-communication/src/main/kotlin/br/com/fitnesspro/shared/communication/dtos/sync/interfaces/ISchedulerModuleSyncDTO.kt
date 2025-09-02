package br.com.fitnesspro.shared.communication.dtos.sync.interfaces

import br.com.fitnesspro.shared.communication.dtos.general.interfaces.IReportDTO
import br.com.fitnesspro.shared.communication.dtos.general.interfaces.ISchedulerReportDTO
import br.com.fitnesspro.shared.communication.dtos.scheduler.interfaces.ISchedulerDTO

interface ISchedulerModuleSyncDTO: ISyncDTO {
    var schedulers: List<ISchedulerDTO>
    var reports: List<IReportDTO>
    var schedulerReports: List<ISchedulerReportDTO>

    override fun isEmpty(): Boolean {
        return schedulers.isEmpty() &&
                reports.isEmpty() &&
                schedulerReports.isEmpty()
    }

    override fun getMaxListSize(): Int {
        return maxOf(
            schedulers.size,
            reports.size,
            schedulerReports.size,
        )
    }

    override fun getItemsCount(): Int {
        return schedulers.size + reports.size + schedulerReports.size
    }
}