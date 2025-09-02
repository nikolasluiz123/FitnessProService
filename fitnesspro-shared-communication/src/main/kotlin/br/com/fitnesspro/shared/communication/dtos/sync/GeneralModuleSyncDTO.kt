package br.com.fitnesspro.shared.communication.dtos.sync

import br.com.fitnesspro.shared.communication.dtos.general.interfaces.IAcademyDTO
import br.com.fitnesspro.shared.communication.dtos.general.interfaces.IPersonAcademyTimeDTO
import br.com.fitnesspro.shared.communication.dtos.general.interfaces.IPersonDTO
import br.com.fitnesspro.shared.communication.dtos.scheduler.interfaces.ISchedulerConfigDTO
import br.com.fitnesspro.shared.communication.dtos.sync.interfaces.IGeneralModuleSyncDTO

class GeneralModuleSyncDTO(
    override var academies: List<IAcademyDTO> = emptyList(),
    override var persons: List<IPersonDTO> = emptyList(),
    override var personAcademyTimes: List<IPersonAcademyTimeDTO> = emptyList(),
    override var schedulerConfigs: List<ISchedulerConfigDTO> = emptyList()
) : IGeneralModuleSyncDTO {
}