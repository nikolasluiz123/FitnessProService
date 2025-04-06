package br.com.fitnesspro.service.service.general

import br.com.fitnesspro.core.enums.EnumDateTimePatterns.*
import br.com.fitnesspro.core.extensions.format
import br.com.fitnesspro.core.extensions.getFirstPartFullDisplayName
import br.com.fitnesspro.service.config.application.cache.ACADEMY_IMPORT_CACHE_NAME
import br.com.fitnesspro.service.config.application.cache.PERSON_ACADEMY_TIME_IMPORT_CACHE_NAME
import br.com.fitnesspro.service.exception.BusinessException
import br.com.fitnesspro.service.models.general.Academy
import br.com.fitnesspro.service.models.general.PersonAcademyTime
import br.com.fitnesspro.service.repository.general.academy.IAcademyRepository
import br.com.fitnesspro.service.repository.general.academy.ICustomAcademyRepository
import br.com.fitnesspro.service.repository.general.person.ICustomPersonAcademyTimeRepository
import br.com.fitnesspro.service.repository.general.person.IPersonAcademyTimeRepository
import br.com.fitnesspro.service.repository.general.person.IPersonRepository
import br.com.fitnesspro.shared.communication.dtos.general.AcademyDTO
import br.com.fitnesspro.shared.communication.dtos.general.PersonAcademyTimeDTO
import br.com.fitnesspro.shared.communication.paging.CommonPageInfos
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.AcademyFilter
import br.com.fitnesspro.shared.communication.query.filter.CommonImportFilter
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class AcademyService(
    private val personAcademyTimeRepository: IPersonAcademyTimeRepository,
    private val customPersonAcademyTimeRepository: ICustomPersonAcademyTimeRepository,
    private val academyRepository: IAcademyRepository,
    private val customAcademyRepository: ICustomAcademyRepository,
    private val personRepository: IPersonRepository
) {

    @CacheEvict(cacheNames = [PERSON_ACADEMY_TIME_IMPORT_CACHE_NAME], allEntries = true)
    fun savePersonAcademyTime(personAcademyTimeDTO: PersonAcademyTimeDTO) {
        val personAcademyTime = personAcademyTimeDTO.toPersonAcademyTime()

        validatePersonAcademyTime(personAcademyTime)

        personAcademyTimeRepository.save(personAcademyTime)
    }

    @Throws(BusinessException::class)
    private fun validatePersonAcademyTime(personAcademyTime: PersonAcademyTime) {
        val conflictPersonAcademyTime = customPersonAcademyTimeRepository.getConflictPersonAcademyTime(
            personAcademyTime.id,
            personAcademyTime.person?.id!!,
            personAcademyTime.dayOfWeek!!,
            personAcademyTime.timeStart!!,
            personAcademyTime.timeEnd!!
        )

        if (conflictPersonAcademyTime != null) {
            throw BusinessException(
                "Não foi possível definir este horário pois há um conflito com o horário de %s das %s até %s.".format(
                    conflictPersonAcademyTime.dayOfWeek!!.getFirstPartFullDisplayName(),
                    conflictPersonAcademyTime.timeStart!!.format(TIME),
                    conflictPersonAcademyTime.timeEnd!!.format(TIME)
                )
            )
        }
    }

    @CacheEvict(cacheNames = [ACADEMY_IMPORT_CACHE_NAME], allEntries = true)
    fun saveAcademy(academyDTO: AcademyDTO) {
        val academy = academyDTO.toAcademy()
        academyRepository.save(academy)

        academyDTO.id = academy.id
    }

    @CacheEvict(cacheNames = [ACADEMY_IMPORT_CACHE_NAME], allEntries = true)
    fun saveAcademyBatch(academyDTOList: List<AcademyDTO>) {
        val academyList = academyDTOList.map { it.toAcademy() }
        academyRepository.saveAll(academyList)
    }

    @CacheEvict(cacheNames = [PERSON_ACADEMY_TIME_IMPORT_CACHE_NAME], allEntries = true)
    fun savePersonAcademyTimeBatch(personAcademyTimeDTOList: List<PersonAcademyTimeDTO>) {
        val personAcademyTimeList = personAcademyTimeDTOList.map { it.toPersonAcademyTime() }
        personAcademyTimeRepository.saveAll(personAcademyTimeList)
    }

    @Cacheable(cacheNames = [PERSON_ACADEMY_TIME_IMPORT_CACHE_NAME], key = "#filter.toCacheKey()")
    fun getPersonAcademyTimesImport(filter: CommonImportFilter, pageInfos: ImportPageInfos): List<PersonAcademyTimeDTO> {
        return customPersonAcademyTimeRepository.getPersonAcademyTimesImport(filter, pageInfos).map { it.toPersonAcademyTimeDTO() }
    }

    @Cacheable(cacheNames = [ACADEMY_IMPORT_CACHE_NAME], key = "#filter.toCacheKey()")
    fun getAcademiesImport(filter: CommonImportFilter, pageInfos: ImportPageInfos): List<AcademyDTO> {
        return customAcademyRepository.getAcademiesImport(filter, pageInfos).map { it.toAcademyDTO() }
    }

    fun getListAcademy(filter: AcademyFilter, pageInfos: CommonPageInfos): List<AcademyDTO> {
        return customAcademyRepository.getListAcademy(filter, pageInfos).map { it.toAcademyDTO() }
    }

    fun getCountListAcademy(filter: AcademyFilter): Int {
        return customAcademyRepository.getCountListAcademy(filter)
    }

    private fun AcademyDTO.toAcademy(): Academy {
        return id?.let {
            academyRepository.findById(it).getOrNull()?.copy(
                name = name,
                phone = phone,
                address = address,
                active = active,
            )
        } ?: Academy(
            name = name,
            phone = phone,
            address = address,
            active = active,
        )
    }

    private fun Academy.toAcademyDTO(): AcademyDTO {
        return AcademyDTO(
            id = id,
            creationDate = creationDate,
            updateDate = updateDate,
            name = name,
            phone = phone,
            address = address,
            active = active,
        )
    }

    private fun PersonAcademyTimeDTO.toPersonAcademyTime(): PersonAcademyTime {
        val personAcademyTime = id?.let { personAcademyTimeRepository.findById(it) }

        return when {
            id == null -> {
                PersonAcademyTime(
                    person = personRepository.findById(personId!!).get(),
                    academy = academyRepository.findById(academyId!!).get(),
                    timeStart = timeStart,
                    timeEnd = timeEnd,
                    dayOfWeek = dayOfWeek,
                    active = active,
                )
            }

            personAcademyTime?.isPresent ?: false -> {
                personAcademyTime!!.get().copy(
                    person = personRepository.findById(personId!!).get(),
                    academy = academyRepository.findById(academyId!!).get(),
                    timeStart = timeStart,
                    timeEnd = timeEnd,
                    dayOfWeek = dayOfWeek,
                    active = active,
                )
            }

            else -> {
                PersonAcademyTime(
                    id = id!!,
                    person = personRepository.findById(personId!!).get(),
                    academy = academyRepository.findById(academyId!!).get(),
                    timeStart = timeStart,
                    timeEnd = timeEnd,
                    dayOfWeek = dayOfWeek,
                    active = active,
                )
            }
        }
    }

    private fun PersonAcademyTime.toPersonAcademyTimeDTO(): PersonAcademyTimeDTO {
        return PersonAcademyTimeDTO(
            id = id,
            creationDate = creationDate,
            updateDate = updateDate,
            personId = person?.id,
            academyId = academy?.id,
            timeStart = timeStart,
            timeEnd = timeEnd,
            dayOfWeek = dayOfWeek,
            active = active,
        )
    }

    fun createMockData() {
        val academies = mutableListOf<AcademyDTO>()

        for (i in 0..2000) {
            academies.add(
                AcademyDTO(
                    name = "Academia $i",
                    address = "Endereço $i",
                    phone = "$i"
                )
            )
        }

        saveAcademyBatch(academies)
    }

}