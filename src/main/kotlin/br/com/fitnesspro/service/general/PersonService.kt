package br.com.fitnesspro.service.general

import br.com.fitnesspro.dto.general.PersonDTO
import br.com.fitnesspro.dto.general.UserDTO
import br.com.fitnesspro.exception.BusinessException
import br.com.fitnesspro.helper.HashHelper
import br.com.fitnesspro.models.general.Person
import br.com.fitnesspro.models.general.User
import br.com.fitnesspro.repository.common.filter.CommonImportFilter
import br.com.fitnesspro.repository.common.paging.ImportPageInfos
import br.com.fitnesspro.repository.general.person.ICustomPersonRepository
import br.com.fitnesspro.repository.general.person.IPersonRepository
import br.com.fitnesspro.repository.general.user.ICustomUserRepository
import br.com.fitnesspro.repository.general.user.IUserRepository
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

    private fun Person.toPersonDTO(): PersonDTO {
        return PersonDTO(
            id = id,
            creationDate = creationDate,
            updateDate = updateDate,
            name = name,
            birthDate = birthDate,
            phone = phone,
            user = user?.toUserDTO()
        )
    }

    private fun PersonDTO.toPerson(): Person {
        return id?.let { personId ->
            personRepository.findById(personId).get().copy(
                name = name,
                birthDate = birthDate,
                phone = phone,
                user = user?.toUser(),
                active = active
            )
        } ?: Person(
            name = name,
            birthDate = birthDate,
            phone = phone,
            user = user?.toUser(),
            active = active
        )
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
            authenticated = authenticated
        )
    }

    private fun UserDTO.toUser(): User {
        return id?.let { userId ->
            userRepository.findById(userId).get().copy(
                email = email,
                password = password,
                type = type,
                active = active
            )
        } ?: User(
            email = email,
            password = password,
            type = type,
            active = active
        )
    }
}