package br.com.fitnesspro.service.service.general

import br.com.fitnesspro.core.helper.HashHelper
import br.com.fitnesspro.service.exception.BusinessException
import br.com.fitnesspro.service.models.general.Person
import br.com.fitnesspro.service.models.general.User
import br.com.fitnesspro.service.repository.general.person.ICustomPersonRepository
import br.com.fitnesspro.service.repository.general.person.IPersonRepository
import br.com.fitnesspro.service.repository.general.user.ICustomUserRepository
import br.com.fitnesspro.service.repository.general.user.IUserRepository
import br.com.fitnesspro.shared.communication.dtos.general.PersonDTO
import br.com.fitnesspro.shared.communication.dtos.general.UserDTO
import br.com.fitnesspro.shared.communication.filter.CommonImportFilter
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import org.springframework.stereotype.Service

@Service
class PersonService(
    private val userRepository: IUserRepository,
    private val personRepository: IPersonRepository,
    private val customUserRepository: ICustomUserRepository,
    private val customPersonRepository: ICustomPersonRepository
) {

    fun savePerson(personDTO: PersonDTO) {
        val person = personDTO.toPerson()

        validateUser(person.user!!)

        val password = person.user?.password!!

        if (!HashHelper.isHashed(password)) {
            person.user?.password = HashHelper.applyHash(password)
        }

        userRepository.save(person.user!!)
        personRepository.save(person)
    }

    @Throws(BusinessException::class)
    private fun validateUser(user: User) {
        if (customUserRepository.isEmailInUse(user.email!!, user.id)) {
            throw BusinessException("Este e-mail já está em uso")
        }
    }

    fun savePersonList(personDTOList: List<PersonDTO>) {
        val persons = personDTOList.map { it.toPerson() }

        persons.forEach { person ->
            validateUser(person.user!!)

            val password = person.user?.password!!

            if (!HashHelper.isHashed(password)) {
                person.user?.password = HashHelper.applyHash(password)
            }
        }

        userRepository.saveAll(persons.map { it.user!! })
        personRepository.saveAll(persons)
    }

    fun getPersonsImport(filter: CommonImportFilter, pageInfos: ImportPageInfos): List<PersonDTO> {
        return customPersonRepository.getPersonsImport(filter, pageInfos).map { it.toPersonDTO() }
    }

    fun getUsersImport(filter: CommonImportFilter, pageInfos: ImportPageInfos): List<UserDTO> {
        return customPersonRepository.getUsersImport(filter, pageInfos).map { it.toUserDTO() }
    }

    private fun Person.toPersonDTO(): PersonDTO {
        return PersonDTO(
            id = id,
            creationDate = creationDate,
            updateDate = updateDate,
            name = name,
            birthDate = birthDate,
            phone = phone,
            user = user?.toUserDTO(),
            creationUserId = creationUser?.id,
            updateUserId = updateUser?.id,
        )
    }

    private fun PersonDTO.toPerson(): Person {
        val person = personRepository.findById(id!!)

        return when {
            id == null -> {
                Person(
                    name = name,
                    birthDate = birthDate,
                    phone = phone,
                    user = user?.toUser(),
                    creationUser = userRepository.findById(creationUserId!!).get(),
                    updateUser = userRepository.findById(updateUserId!!).get()
                )
            }

            person.isPresent -> {
                person.get().copy(
                    name = name,
                    birthDate = birthDate,
                    phone = phone,
                    user = user?.toUser(),
                    updateUser = userRepository.findById(updateUserId!!).get()
                )
            }

            else -> {
                Person(
                    id = id!!,
                    name = name,
                    birthDate = birthDate,
                    phone = phone,
                    user = user?.toUser(),
                    creationUser = userRepository.findById(creationUserId!!).get(),
                    updateUser = userRepository.findById(updateUserId!!).get()
                )
            }
        }
    }

    private fun User.toUserDTO(): UserDTO {
        return UserDTO(
            id = id,
            creationDate = creationDate,
            updateDate = updateDate,
            active = active,
            email = email,
            password = password,
            type = type,
            authenticated = authenticated,
            creationUserId = creationUser?.id,
            updateUserId = updateUser?.id,
        )
    }

    private fun UserDTO.toUser(): User {
        val user = userRepository.findById(id!!)

        return when {
            id == null -> {
                User(
                    email = email,
                    password = password,
                    type = type,
                    active = active,
                    creationUser = userRepository.findById(creationUserId!!).get(),
                    updateUser = userRepository.findById(updateUserId!!).get()
                )
            }

            user.isPresent -> {
                user.get().copy(
                    email = email,
                    password = password,
                    type = type,
                    active = active,
                    updateUser = userRepository.findById(updateUserId!!).get()
                )
            }

            else -> {
                User(
                    id = id!!,
                    email = email,
                    password = password,
                    type = type,
                    active = active,
                    creationUser = userRepository.findById(creationUserId!!).get(),
                    updateUser = userRepository.findById(updateUserId!!).get()
                )
            }
        }
    }
}