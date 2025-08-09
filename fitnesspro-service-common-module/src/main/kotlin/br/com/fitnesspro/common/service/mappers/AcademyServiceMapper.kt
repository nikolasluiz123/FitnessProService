package br.com.fitnesspro.common.service.mappers

import br.com.fitnesspro.common.repository.auditable.general.IAcademyRepository
import br.com.fitnesspro.common.repository.auditable.general.IPersonAcademyTimeRepository
import br.com.fitnesspro.authentication.repository.auditable.IPersonRepository
import br.com.fitnesspro.models.general.Academy
import br.com.fitnesspro.models.general.PersonAcademyTime
import br.com.fitnesspro.shared.communication.dtos.general.AcademyDTO
import br.com.fitnesspro.shared.communication.dtos.general.PersonAcademyTimeDTO
import org.springframework.stereotype.Service

@Service
class AcademyServiceMapper(
    private val personRepository: IPersonRepository,
    private val academyRepository: IAcademyRepository,
    private val personAcademyTimeRepository: IPersonAcademyTimeRepository,
) {
    fun getAcademy(dto: AcademyDTO): Academy {
        return if (dto.id != null) {
            academyRepository.findById(dto.id!!).get().copy(
                name = dto.name,
                phone = dto.phone,
                address = dto.address,
                active = dto.active,
            )
        } else {
            Academy(
                name = dto.name,
                phone = dto.phone,
                address = dto.address,
                active = dto.active,
            )
        }
    }

    fun getAcademyDTO(model: Academy): AcademyDTO {
        return AcademyDTO(
            id = model.id,
            creationDate = model.creationDate,
            updateDate = model.updateDate,
            name = model.name,
            phone = model.phone,
            address = model.address,
            active = model.active,
        )
    }

    fun getPersonAcademyTime(dto: PersonAcademyTimeDTO): PersonAcademyTime {
        val personAcademyTime = dto.id?.let { personAcademyTimeRepository.findById(it) }

        return when {
            dto.id == null -> {
                PersonAcademyTime(
                    person = personRepository.findById(dto.personId!!).get(),
                    academy = academyRepository.findById(dto.academyId!!).get(),
                    timeStart = dto.timeStart,
                    timeEnd = dto.timeEnd,
                    dayOfWeek = dto.dayOfWeek,
                    active = dto.active,
                )
            }

            personAcademyTime?.isPresent == true -> {
                personAcademyTime.get().copy(
                    person = personRepository.findById(dto.personId!!).get(),
                    academy = academyRepository.findById(dto.academyId!!).get(),
                    timeStart = dto.timeStart,
                    timeEnd = dto.timeEnd,
                    dayOfWeek = dto.dayOfWeek,
                    active = dto.active,
                )
            }

            else -> {
                PersonAcademyTime(
                    id = dto.id!!,
                    person = personRepository.findById(dto.personId!!).get(),
                    academy = academyRepository.findById(dto.academyId!!).get(),
                    timeStart = dto.timeStart,
                    timeEnd = dto.timeEnd,
                    dayOfWeek = dto.dayOfWeek,
                    active = dto.active,
                )
            }
        }
    }

    fun getPersonAcademyTimeDTO(model: PersonAcademyTime): PersonAcademyTimeDTO {
        return PersonAcademyTimeDTO(
            id = model.id,
            creationDate = model.creationDate,
            updateDate = model.updateDate,
            personId = model.person?.id,
            academyId = model.academy?.id,
            timeStart = model.timeStart,
            timeEnd = model.timeEnd,
            dayOfWeek = model.dayOfWeek,
            active = model.active,
        )
    }
}