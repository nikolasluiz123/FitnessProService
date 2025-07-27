package br.com.fitnesspro.services.general

import br.com.fitnesspro.config.application.cache.ACADEMY_IMPORT_CACHE_NAME
import br.com.fitnesspro.config.application.cache.PERSON_ACADEMY_TIME_IMPORT_CACHE_NAME
import br.com.fitnesspro.repository.auditable.general.IAcademyRepository
import br.com.fitnesspro.repository.auditable.general.IPersonAcademyTimeRepository
import br.com.fitnesspro.repository.jpa.general.academy.ICustomAcademyRepository
import br.com.fitnesspro.repository.jpa.general.person.ICustomPersonAcademyTimeRepository
import br.com.fitnesspro.services.mappers.AcademyServiceMapper
import br.com.fitnesspro.shared.communication.dtos.general.AcademyDTO
import br.com.fitnesspro.shared.communication.dtos.general.PersonAcademyTimeDTO
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
    fun saveAcademy(academyDTO: AcademyDTO) {
        val academy = academyServiceMapper.getAcademy(academyDTO)
        academyRepository.save(academy)

        academyDTO.id = academy.id
    }

    @CacheEvict(cacheNames = [ACADEMY_IMPORT_CACHE_NAME], allEntries = true)
    fun saveAcademyBatch(academyDTOList: List<AcademyDTO>) {
        val academyList = academyDTOList.map(academyServiceMapper::getAcademy)
        academyRepository.saveAll(academyList)
    }

    @CacheEvict(cacheNames = [PERSON_ACADEMY_TIME_IMPORT_CACHE_NAME], allEntries = true)
    fun savePersonAcademyTimeBatch(personAcademyTimeDTOList: List<PersonAcademyTimeDTO>) {
        val personAcademyTimeList = personAcademyTimeDTOList.map(academyServiceMapper::getPersonAcademyTime)
        personAcademyTimeRepository.saveAll(personAcademyTimeList)
    }

    @Cacheable(cacheNames = [PERSON_ACADEMY_TIME_IMPORT_CACHE_NAME], key = "#filter.toCacheKey()")
    fun getPersonAcademyTimesImport(filter: CommonImportFilter, pageInfos: ImportPageInfos): List<PersonAcademyTimeDTO> {
        return customPersonAcademyTimeRepository.getPersonAcademyTimesImport(filter, pageInfos).map(academyServiceMapper::getPersonAcademyTimeDTO)
    }

    @Cacheable(cacheNames = [ACADEMY_IMPORT_CACHE_NAME], key = "#filter.toCacheKey()")
    fun getAcademiesImport(filter: CommonImportFilter, pageInfos: ImportPageInfos): List<AcademyDTO> {
        return customAcademyRepository.getAcademiesImport(filter, pageInfos).map(academyServiceMapper::getAcademyDTO)
    }

    fun getListAcademy(filter: AcademyFilter, pageInfos: CommonPageInfos): List<AcademyDTO> {
        return customAcademyRepository.getListAcademy(filter, pageInfos).map(academyServiceMapper::getAcademyDTO)
    }

    fun getCountListAcademy(filter: AcademyFilter): Int {
        return customAcademyRepository.getCountListAcademy(filter)
    }

}