package br.com.fitnesspro.service.general

import br.com.fitnesspro.dto.general.AcademyDTO
import br.com.fitnesspro.dto.general.PersonAcademyTimeDTO
import br.com.fitnesspro.enums.EnumDateTimePatterns
import br.com.fitnesspro.exception.BusinessException
import br.com.fitnesspro.extensions.format
import br.com.fitnesspro.extensions.getFirstPartFullDisplayName
import br.com.fitnesspro.models.general.Academy
import br.com.fitnesspro.models.general.PersonAcademyTime
import br.com.fitnesspro.repository.general.academy.IAcademyRepository
import br.com.fitnesspro.repository.general.person.ICustomPersonAcademyTimeRepository
import br.com.fitnesspro.repository.general.person.IPersonAcademyTimeRepository
import br.com.fitnesspro.repository.general.person.IPersonRepository
import org.springframework.stereotype.Service

@Service
class AcademyService(
    private val personAcademyTimeRepository: IPersonAcademyTimeRepository,
    private val customPersonAcademyTimeRepository: ICustomPersonAcademyTimeRepository,
    private val academyRepository: IAcademyRepository,
    private val personRepository: IPersonRepository
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
                    conflictPersonAcademyTime.timeStart!!.format(EnumDateTimePatterns.TIME),
                    conflictPersonAcademyTime.timeEnd!!.format(EnumDateTimePatterns.TIME)
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

    private fun AcademyDTO.toAcademy(): Academy {
        return id?.let { academyId ->
            academyRepository.findById(academyId).get().copy(
                name = name,
                phone = phone,
                address = address,
                active = active
            )
        } ?: Academy(
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