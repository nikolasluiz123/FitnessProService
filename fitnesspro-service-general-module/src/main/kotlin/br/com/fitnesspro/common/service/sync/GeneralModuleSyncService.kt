package br.com.fitnesspro.common.service.sync

import br.com.fitnesspro.authentication.service.PersonService
import br.com.fitnesspro.authentication.service.SchedulerConfigService
import br.com.fitnesspro.common.service.general.AcademyService
import br.com.fitnesspro.core.cache.*
import br.com.fitnesspro.service.communication.dtos.sync.ValidatedGeneralModuleSyncDTO
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.CommonImportFilter
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class GeneralModuleSyncService(
    private val academyService: AcademyService,
    private val personService: PersonService,
    private val schedulerConfigService: SchedulerConfigService
) {

    fun getImportationData(filter: CommonImportFilter, pageInfos: ImportPageInfos): ValidatedGeneralModuleSyncDTO {
        val academies = academyService.getAcademiesImport(filter, pageInfos)
        val persons = personService.getPersonsImport(filter, pageInfos)

        val academyIds = academies.mapNotNull { it.id }
        val personIds = persons.mapNotNull { it.id }

        val personAcademyTimes = academyService.getPersonAcademyTimesImport(filter, pageInfos, personIds, academyIds)
        val schedulerConfigs = schedulerConfigService.getSchedulerConfigsImport(filter, pageInfos, personIds)

        return ValidatedGeneralModuleSyncDTO(
            academies = academies,
            persons = persons,
            personAcademyTimes = personAcademyTimes,
            schedulerConfigs = schedulerConfigs
        )
    }

    fun saveExportedData(syncDTO: ValidatedGeneralModuleSyncDTO) {
        academyService.saveAcademyBatch(syncDTO.academies)
        personService.savePersonBatch(syncDTO.persons)
        academyService.savePersonAcademyTimeBatch(syncDTO.personAcademyTimes)
        schedulerConfigService.saveSchedulerConfigBatch(syncDTO.schedulerConfigs)
    }
}