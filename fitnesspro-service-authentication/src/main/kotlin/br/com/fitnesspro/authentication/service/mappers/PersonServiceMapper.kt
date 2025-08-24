package br.com.fitnesspro.authentication.service.mappers

import br.com.fitnesspro.authentication.repository.auditable.IPersonRepository
import br.com.fitnesspro.models.general.Person
import br.com.fitnesspro.service.communication.dtos.general.ValidatedPersonDTO
import org.springframework.stereotype.Service

@Service
class PersonServiceMapper(
    private val personRepository: IPersonRepository,
    private val userServiceMapper: UserServiceMapper
) {

    fun getPersonDTO(model: Person): ValidatedPersonDTO {
        return ValidatedPersonDTO(
            id = model.id,
            creationDate = model.creationDate,
            updateDate = model.updateDate,
            name = model.name,
            birthDate = model.birthDate,
            phone = model.phone,
            user = userServiceMapper.getUserDTO(model.user!!),
        )
    }

    fun getPerson(dto: ValidatedPersonDTO): Person {
        val person = dto.id?.let { personRepository.findById(it) }

        return when {
            dto.id == null -> {
                Person(
                    name = dto.name,
                    birthDate = dto.birthDate,
                    phone = dto.phone,
                    user = userServiceMapper.getUser(dto.user!!),
                    active = dto.active
                )
            }

            person?.isPresent == true -> {
                person.get().copy(
                    name = dto.name,
                    birthDate = dto.birthDate,
                    phone = dto.phone,
                    user = userServiceMapper.getUser(dto.user!!),
                    active = dto.active
                )
            }

            else -> {
                Person(
                    id = dto.id!!,
                    name = dto.name,
                    birthDate = dto.birthDate,
                    phone = dto.phone,
                    user = userServiceMapper.getUser(dto.user!!),
                    active = dto.active
                )
            }
        }
    }
}