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

    @Cacheable(
        cacheNames = [
            ACADEMY_IMPORT_CACHE_NAME,
            PERSON_IMPORT_CACHE_NAME,
            PERSON_USER_IMPORT_CACHE_NAME,
            PERSON_ACADEMY_TIME_IMPORT_CACHE_NAME,
            SCHEDULER_CONFIG_IMPORT_CACHE_NAME
        ],
        key = "#filter.toCacheKey()"
    )
    fun getImportationData(filter: CommonImportFilter, pageInfos: ImportPageInfos): ValidatedGeneralModuleSyncDTO {
        return ValidatedGeneralModuleSyncDTO(
            academies = academyService.getAcademiesImport(filter, pageInfos),
            persons = personService.getPersonsImport(filter, pageInfos),
            personAcademyTimes = academyService.getPersonAcademyTimesImport(filter, pageInfos),
            schedulerConfigs = schedulerConfigService.getSchedulerConfigsImport(filter, pageInfos)
        )
    }

    fun saveExportedData(syncDTO: ValidatedGeneralModuleSyncDTO) {
        academyService.saveAcademyBatch(syncDTO.academies)
        personService.savePersonBatch(syncDTO.persons)
        academyService.savePersonAcademyTimeBatch(syncDTO.personAcademyTimes)
        schedulerConfigService.saveSchedulerConfigBatch(syncDTO.schedulerConfigs)
    }
}