package br.com.fitnesspro.service.communication.dtos.sync

import br.com.fitnesspro.shared.communication.dtos.general.interfaces.IAcademyDTO
import br.com.fitnesspro.shared.communication.dtos.general.interfaces.IPersonAcademyTimeDTO
import br.com.fitnesspro.shared.communication.dtos.general.interfaces.IPersonDTO
import br.com.fitnesspro.shared.communication.dtos.scheduler.interfaces.ISchedulerConfigDTO
import br.com.fitnesspro.shared.communication.dtos.sync.interfaces.IGeneralModuleSyncDTO
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Classe DTO sincronismo do módulo geral")
class ValidatedGeneralModuleSyncDTO(
    override var academies: List<IAcademyDTO> = emptyList(),
    override var persons: List<IPersonDTO> = emptyList(),
    override var personAcademyTimes: List<IPersonAcademyTimeDTO> = emptyList(),
    override var schedulerConfigs: List<ISchedulerConfigDTO> = emptyList()
) : IGeneralModuleSyncDTO