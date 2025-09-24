package br.com.fitnesspro.service.communication.dtos.sync

import br.com.fitnesspro.service.communication.dtos.annotation.EntityReference
import br.com.fitnesspro.shared.communication.dtos.general.interfaces.IAcademyDTO
import br.com.fitnesspro.shared.communication.dtos.general.interfaces.IPersonAcademyTimeDTO
import br.com.fitnesspro.shared.communication.dtos.general.interfaces.IPersonDTO
import br.com.fitnesspro.shared.communication.dtos.scheduler.interfaces.ISchedulerConfigDTO
import br.com.fitnesspro.shared.communication.dtos.sync.interfaces.IGeneralModuleSyncDTO
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Classe DTO sincronismo do módulo geral")
class ValidatedGeneralModuleSyncDTO(
    @EntityReference("Academy")
    override var academies: List<IAcademyDTO> = emptyList(),

    @EntityReference("Person")
    override var persons: List<IPersonDTO> = emptyList(),

    @EntityReference("PersonAcademyTime")
    override var personAcademyTimes: List<IPersonAcademyTimeDTO> = emptyList(),

    @EntityReference("SchedulerConfig")
    override var schedulerConfigs: List<ISchedulerConfigDTO> = emptyList()
) : IGeneralModuleSyncDTO