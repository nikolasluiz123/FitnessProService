package br.com.fitnesspro.service.service.general

import br.com.fitnesspro.core.helper.HashHelper
import br.com.fitnesspro.service.config.application.cache.PERSON_IMPORT_CACHE_NAME
import br.com.fitnesspro.service.config.application.cache.PERSON_USER_IMPORT_CACHE_NAME
import br.com.fitnesspro.service.exception.BusinessException
import br.com.fitnesspro.service.models.general.Person
import br.com.fitnesspro.service.models.general.User
import br.com.fitnesspro.service.repository.general.person.ICustomPersonRepository
import br.com.fitnesspro.service.repository.general.person.IPersonRepository
import br.com.fitnesspro.service.repository.general.user.ICustomUserRepository
import br.com.fitnesspro.service.repository.general.user.IUserRepository
import br.com.fitnesspro.service.service.firebase.FirebaseAuthenticationService
import br.com.fitnesspro.service.service.scheduler.SchedulerService
import br.com.fitnesspro.shared.communication.dtos.general.PersonDTO
import br.com.fitnesspro.shared.communication.dtos.general.UserDTO
import br.com.fitnesspro.shared.communication.dtos.scheduler.SchedulerConfigDTO
import br.com.fitnesspro.shared.communication.paging.CommonPageInfos
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.CommonImportFilter
import br.com.fitnesspro.shared.communication.query.filter.PersonFilter
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class PersonService(
    private val userRepository: IUserRepository,
    private val personRepository: IPersonRepository,
    private val customUserRepository: ICustomUserRepository,
    private val customPersonRepository: ICustomPersonRepository,
    private val schedulerService: SchedulerService,
    private val firebaseAuthenticationService: FirebaseAuthenticationService
) {

    @CacheEvict(cacheNames = [PERSON_IMPORT_CACHE_NAME, PERSON_USER_IMPORT_CACHE_NAME], allEntries = true)
    fun savePerson(personDTO: PersonDTO) {
        val person = personDTO.toPerson()

        if (personDTO.active) {
            validateUser(person.user!!)

            val password = person.user?.password!!

            if (!HashHelper.isHashed(password)) {
                person.user?.password = HashHelper.applyHash(password)
            }
        }

        userRepository.save(person.user!!)
        personRepository.save(person)

        if (personDTO.active) {
            firebaseAuthenticationService.saveUser(personDTO)
        } else {
            firebaseAuthenticationService.deleteUser(personDTO)
        }

        if (personDTO.createDefaultSchedulerConfig) {
            schedulerService.saveSchedulerConfig(SchedulerConfigDTO(personId = person.id))
        }

        personDTO.id = person.id
        personDTO.user?.id = person.user?.id
    }

    @Throws(BusinessException::class)
    private fun validateUser(user: User) {
        if (customUserRepository.isEmailInUse(user.email!!, user.id)) {
            throw BusinessException("Este e-mail já está em uso")
        }

        if (user.password.length < 6) {
            throw BusinessException("Sua senha deve possuir pelo menos 6 caracteres")
        }
    }

    @CacheEvict(cacheNames = [PERSON_IMPORT_CACHE_NAME, PERSON_USER_IMPORT_CACHE_NAME], allEntries = true)
    fun savePersonList(personDTOList: List<PersonDTO>) {
        val persons = personDTOList.map { personDTO ->
            val person = personDTO.toPerson()

            validateUser(person.user!!)

            val password = person.user?.password!!

            if (!HashHelper.isHashed(password)) {
                person.user?.password = HashHelper.applyHash(password)
            }

            if (personDTO.active) {
                firebaseAuthenticationService.saveUser(personDTO)
            } else {
                firebaseAuthenticationService.deleteUser(personDTO)
            }

            person
        }

        userRepository.saveAll(persons.map { it.user!! })
        personRepository.saveAll(persons)
    }

    @Cacheable(cacheNames = [PERSON_IMPORT_CACHE_NAME], key = "#filter.toCacheKey()")
    fun getPersonsImport(filter: CommonImportFilter, pageInfos: ImportPageInfos): List<PersonDTO> {
        return customPersonRepository.getPersonsImport(filter, pageInfos).map { it.toPersonDTO() }
    }

    @Cacheable(cacheNames = [PERSON_USER_IMPORT_CACHE_NAME], key = "#filter.toCacheKey()")
    fun getUsersImport(filter: CommonImportFilter, pageInfos: ImportPageInfos): List<UserDTO> {
        return customPersonRepository.getUsersImport(filter, pageInfos).map { it.toUserDTO() }
    }

    fun getListPersons(filter: PersonFilter, pageInfos: CommonPageInfos): List<PersonDTO> {
        return customPersonRepository.getListPersons(filter, pageInfos)
    }

    fun getCountListPersons(filter: PersonFilter): Int {
        return customPersonRepository.getCountListPersons(filter)
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
        )
    }

    private fun PersonDTO.toPerson(): Person {
        val person = id?.let { personRepository.findById(it) }

        return when {
            id == null -> {
                Person(
                    name = name,
                    birthDate = birthDate,
                    phone = phone,
                    user = user?.toUser(),
                    active = active
                )
            }

            person?.isPresent ?: false -> {
                person!!.get().copy(
                    name = name,
                    birthDate = birthDate,
                    phone = phone,
                    user = user?.toUser(),
                    active = active
                )
            }

            else -> {
                Person(
                    id = id!!,
                    name = name,
                    birthDate = birthDate,
                    phone = phone,
                    user = user?.toUser(),
                    active = active
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
        )
    }

    private fun UserDTO.toUser(): User {
        val user = id?.let { userRepository.findById(it) }

        return when {
            id == null -> {
                User(
                    email = email,
                    password = password,
                    type = type,
                    active = active,
                )
            }

            user?.isPresent ?: false -> {
                user!!.get().copy(
                    email = email,
                    password = password,
                    type = type,
                    active = active,
                )
            }

            else -> {
                User(
                    id = id!!,
                    email = email,
                    password = password,
                    type = type,
                    active = active,
                )
            }
        }
    }

    fun getPersonByEmail(email: String): PersonDTO? {
        return customPersonRepository.findByEmail(email)?.toPersonDTO()
    }
}