package br.com.fitnesspro.shared.communication.dtos.sync

import br.com.fitnesspro.shared.communication.dtos.general.interfaces.IReportDTO
import br.com.fitnesspro.shared.communication.dtos.general.interfaces.ISchedulerReportDTO
import br.com.fitnesspro.shared.communication.dtos.scheduler.interfaces.ISchedulerDTO
import br.com.fitnesspro.shared.communication.dtos.sync.interfaces.ISchedulerModuleSyncDTO

class SchedulerModuleSyncDTO(
    override var schedulers: List<ISchedulerDTO> = emptyList(),
    override var reports: List<IReportDTO> = emptyList(),
    override var schedulerReports: List<ISchedulerReportDTO> = emptyList()
) : ISchedulerModuleSyncDTO {
}