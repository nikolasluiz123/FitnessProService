package br.com.fitnesspro.common.service.general

import br.com.fitnesspro.authentication.repository.jpa.ICustomPersonAcademyTimeRepository
import br.com.fitnesspro.common.repository.auditable.general.IAcademyRepository
import br.com.fitnesspro.common.repository.auditable.general.IPersonAcademyTimeRepository
import br.com.fitnesspro.common.repository.jpa.general.academy.ICustomAcademyRepository
import br.com.fitnesspro.common.service.mappers.AcademyServiceMapper
import br.com.fitnesspro.core.cache.ACADEMY_IMPORT_CACHE_NAME
import br.com.fitnesspro.core.cache.PERSON_ACADEMY_TIME_IMPORT_CACHE_NAME
import br.com.fitnesspro.service.communication.dtos.general.ValidatedAcademyDTO
import br.com.fitnesspro.service.communication.dtos.general.ValidatedPersonAcademyTimeDTO
import br.com.fitnesspro.shared.communication.dtos.general.interfaces.IAcademyDTO
import br.com.fitnesspro.shared.communication.dtos.general.interfaces.IPersonAcademyTimeDTO
import br.com.fitnesspro.shared.communication.paging.CommonPageInfos
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.AcademyFilter
import br.com.fitnesspro.shared.communication.query.filter.importation.CommonImportFilter
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class AcademyService(
    private val personAcademyTimeRepository: IPersonAcademyTimeRepository,
    private val customPersonAcademyTimeRepository: ICustomPersonAcademyTimeRepository,
    private val academyRepository: IAcademyRepository,
    private val customAcademyRepository: ICustomAcademyRepository,
    private val academyServiceMapper: AcademyServiceMapper,
) {
    @CacheEvict(cacheNames = [ACADEMY_IMPORT_CACHE_NAME], allEntries = true)
    fun saveAcademy(validatedAcademyDTO: ValidatedAcademyDTO) {
        val academy = academyServiceMapper.getAcademy(validatedAcademyDTO)
        academyRepository.save(academy)

        validatedAcademyDTO.id = academy.id
    }

    @CacheEvict(cacheNames = [ACADEMY_IMPORT_CACHE_NAME], allEntries = true)
    fun saveAcademyBatch(list: List<IAcademyDTO>) {
        val academyList = list.map(academyServiceMapper::getAcademy)
        academyRepository.saveAll(academyList)
    }

    @CacheEvict(cacheNames = [PERSON_ACADEMY_TIME_IMPORT_CACHE_NAME], allEntries = true)
    fun savePersonAcademyTimeBatch(list: List<IPersonAcademyTimeDTO>) {
        val personAcademyTimeList = list.map(academyServiceMapper::getPersonAcademyTime)
        personAcademyTimeRepository.saveAll(personAcademyTimeList)
    }

    @Cacheable(cacheNames = [PERSON_ACADEMY_TIME_IMPORT_CACHE_NAME], key = "#filter.toCacheKey()")
    fun getPersonAcademyTimesImport(filter: CommonImportFilter, pageInfos: ImportPageInfos): List<ValidatedPersonAcademyTimeDTO> {
        return customPersonAcademyTimeRepository.getPersonAcademyTimesImport(filter, pageInfos).map(academyServiceMapper::getValidatedPersonAcademyTimeDTO)
    }

    @Cacheable(cacheNames = [ACADEMY_IMPORT_CACHE_NAME], key = "#filter.toCacheKey()")
    fun getAcademiesImport(filter: CommonImportFilter, pageInfos: ImportPageInfos): List<ValidatedAcademyDTO> {
        return customAcademyRepository.getAcademiesImport(filter, pageInfos).map(academyServiceMapper::getValidatedAcademyDTO)
    }

    fun getListAcademy(filter: AcademyFilter, pageInfos: CommonPageInfos): List<ValidatedAcademyDTO> {
        return customAcademyRepository.getListAcademy(filter, pageInfos).map(academyServiceMapper::getValidatedAcademyDTO)
    }

    fun getCountListAcademy(filter: AcademyFilter): Int {
        return customAcademyRepository.getCountListAcademy(filter)
    }

}