package br.com.fitnesspro.service.communication.dtos.sync

import br.com.fitnesspro.shared.communication.dtos.general.interfaces.IReportDTO
import br.com.fitnesspro.shared.communication.dtos.general.interfaces.ISchedulerReportDTO
import br.com.fitnesspro.shared.communication.dtos.scheduler.interfaces.ISchedulerDTO
import br.com.fitnesspro.shared.communication.dtos.sync.interfaces.ISchedulerModuleSyncDTO
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Classe DTO sincronismo do módulo de agenda")
class ValidatedSchedulerModuleSyncDTO(
    override var schedulers: List<ISchedulerDTO> = emptyList(),
    override var reports: List<IReportDTO> = emptyList(),
    override var schedulerReports: List<ISchedulerReportDTO> = emptyList()
) : ISchedulerModuleSyncDTO