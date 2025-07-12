package br.com.fitnesspro.services.mappers

import br.com.fitnesspro.models.general.Person
import br.com.fitnesspro.models.general.User
import br.com.fitnesspro.repository.auditable.general.IPersonRepository
import br.com.fitnesspro.repository.auditable.general.IUserRepository
import br.com.fitnesspro.shared.communication.dtos.general.PersonDTO
import br.com.fitnesspro.shared.communication.dtos.general.UserDTO
import org.springframework.stereotype.Service

@Service
class PersonServiceMapper(
    private val personRepository: IPersonRepository,
    private val userRepository: IUserRepository,
) {

    fun getPersonDTO(model: Person): PersonDTO {
        return PersonDTO(
            id = model.id,
            creationDate = model.creationDate,
            updateDate = model.updateDate,
            name = model.name,
            birthDate = model.birthDate,
            phone = model.phone,
            user = getUserDTO(model.user!!),
        )
    }

    fun getPerson(dto: PersonDTO): Person {
        val person = dto.id?.let { personRepository.findById(it) }

        return when {
            dto.id == null -> {
                Person(
                    name = dto.name,
                    birthDate = dto.birthDate,
                    phone = dto.phone,
                    user = getUser(dto.user!!),
                    active = dto.active
                )
            }

            person?.isPresent == true -> {
                person.get().copy(
                    name = dto.name,
                    birthDate = dto.birthDate,
                    phone = dto.phone,
                    user = getUser(dto.user!!),
                    active = dto.active
                )
            }

            else -> {
                Person(
                    id = dto.id!!,
                    name = dto.name,
                    birthDate = dto.birthDate,
                    phone = dto.phone,
                    user = getUser(dto.user!!),
                    active = dto.active
                )
            }
        }
    }

    fun getUserDTO(model: User): UserDTO {
        return UserDTO(
            id = model.id,
            creationDate = model.creationDate,
            updateDate = model.updateDate,
            active = model.active,
            email = model.email,
            password = model.password,
            type = model.type,
        )
    }

    fun getUser(dto: UserDTO): User {
        val user = dto.id?.let { userRepository.findById(it) }

        return when {
            dto.id == null -> {
                User(
                    email = dto.email,
                    password = dto.password,
                    type = dto.type,
                    active = dto.active,
                )
            }

            user?.isPresent == true -> {
                user.get().copy(
                    email = dto.email,
                    password = dto.password,
                    type = dto.type,
                    active = dto.active,
                )
            }

            else -> {
                User(
                    id = dto.id!!,
                    email = dto.email,
                    password = dto.password,
                    type = dto.type,
                    active = dto.active,
                )
            }
        }
    }
}