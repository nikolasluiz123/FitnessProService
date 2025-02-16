package br.com.fitnesspro.service.service.general

import br.com.fitnesspro.core.enums.EnumDateTimePatterns.*
import br.com.fitnesspro.core.extensions.format
import br.com.fitnesspro.core.extensions.getFirstPartFullDisplayName
import br.com.fitnesspro.service.exception.BusinessException
import br.com.fitnesspro.service.models.general.PersonAcademyTime
import br.com.fitnesspro.shared.communication.filter.CommonImportFilter
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.service.repository.general.academy.IAcademyRepository
import br.com.fitnesspro.service.repository.general.academy.ICustomAcademyRepository
import br.com.fitnesspro.service.repository.general.person.ICustomPersonAcademyTimeRepository
import br.com.fitnesspro.service.repository.general.person.IPersonAcademyTimeRepository
import br.com.fitnesspro.service.repository.general.person.IPersonRepository
import br.com.fitnesspro.shared.communication.dtos.general.AcademyDTO
import br.com.fitnesspro.shared.communication.dtos.general.PersonAcademyTimeDTO
import org.springframework.stereotype.Service

@Service
class AcademyService(
    private val personAcademyTimeRepository: IPersonAcademyTimeRepository,
    private val customPersonAcademyTimeRepository: ICustomPersonAcademyTimeRepository,
    private val academyRepository: IAcademyRepository,
    private val customAcademyRepository: ICustomAcademyRepository,
    private val personRepository: IPersonRepository,
) {

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

    fun saveAcademy(academyDTO: AcademyDTO) {
        val academy = academyDTO.toAcademy()
        academyRepository.save(academy)
    }

    fun saveAcademyBatch(academyDTOList: List<AcademyDTO>) {
        val academyList = academyDTOList.map { it.toAcademy() }
        academyRepository.saveAll(academyList)
    }

    fun savePersonAcademyTimeBatch(personAcademyTimeDTOList: List<PersonAcademyTimeDTO>) {
        val personAcademyTimeList = personAcademyTimeDTOList.map { it.toPersonAcademyTime() }
        personAcademyTimeRepository.saveAll(personAcademyTimeList)
    }

    fun getPersonAcademyTimesImport(filter: CommonImportFilter, pageInfos: ImportPageInfos): List<PersonAcademyTimeDTO> {
        return customPersonAcademyTimeRepository.getPersonAcademyTimesImport(filter, pageInfos)
    }

    fun getAcademiesImport(filter: CommonImportFilter, pageInfos: ImportPageInfos): List<AcademyDTO> {
        return customAcademyRepository.getAcademiesImport(filter, pageInfos)
    }

    private fun AcademyDTO.toAcademy(): br.com.fitnesspro.service.models.general.Academy {
        return id?.let { academyId ->
            academyRepository.findById(academyId).get().copy(
                name = name,
                phone = phone,
                address = address,
                active = active
            )
        } ?: br.com.fitnesspro.service.models.general.Academy(
            name = name,
            phone = phone,
            address = address,
            active = active
        )
    }

    private fun PersonAcademyTimeDTO.toPersonAcademyTime(): PersonAcademyTime {
        return id?.let { personAcademyTimeId ->
            personAcademyTimeRepository.findById(personAcademyTimeId).get().copy(
                person = personRepository.findById(personId!!).get(),
                academy = academyRepository.findById(academyId!!).get(),
                timeStart = timeStart,
                timeEnd = timeEnd,
                dayOfWeek = dayOfWeek,
                active = active
            )
        } ?: PersonAcademyTime(
            person = personRepository.findById(personId!!).get(),
            academy = academyRepository.findById(academyId!!).get(),
            timeStart = timeStart,
            timeEnd = timeEnd,
            dayOfWeek = dayOfWeek,
            active = active
        )
    }
}