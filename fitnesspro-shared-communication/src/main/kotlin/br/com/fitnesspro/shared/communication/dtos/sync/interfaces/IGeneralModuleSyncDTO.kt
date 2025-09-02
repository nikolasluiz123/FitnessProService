package br.com.fitnesspro.shared.communication.dtos.sync.interfaces

import br.com.fitnesspro.shared.communication.dtos.general.interfaces.IAcademyDTO
import br.com.fitnesspro.shared.communication.dtos.general.interfaces.IPersonAcademyTimeDTO
import br.com.fitnesspro.shared.communication.dtos.general.interfaces.IPersonDTO
import br.com.fitnesspro.shared.communication.dtos.scheduler.interfaces.ISchedulerConfigDTO

interface IGeneralModuleSyncDTO: ISyncDTO {
    var academies: List<IAcademyDTO>
    var persons: List<IPersonDTO>
    var personAcademyTimes: List<IPersonAcademyTimeDTO>
    var schedulerConfigs: List<ISchedulerConfigDTO>

    override fun isEmpty(): Boolean {
        return academies.isEmpty() &&
                persons.isEmpty() &&
                personAcademyTimes.isEmpty() &&
                schedulerConfigs.isEmpty()
    }

    override fun getMaxListSize(): Int {
        return maxOf(
            academies.size,
            persons.size,
            personAcademyTimes.size,
            schedulerConfigs.size
        )
    }

    override fun getItemsCount(): Int {
        return academies.size + persons.size + personAcademyTimes.size + schedulerConfigs.size
    }
}