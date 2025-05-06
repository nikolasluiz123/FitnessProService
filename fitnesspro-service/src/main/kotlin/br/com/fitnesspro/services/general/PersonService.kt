package br.com.fitnesspro.services.general

import br.com.fitnesspro.config.application.cache.PERSON_IMPORT_CACHE_NAME
import br.com.fitnesspro.config.application.cache.PERSON_USER_IMPORT_CACHE_NAME
import br.com.fitnesspro.exception.BusinessException
import br.com.fitnesspro.models.general.Person
import br.com.fitnesspro.models.general.User
import br.com.fitnesspro.repository.general.person.ICustomPersonRepository
import br.com.fitnesspro.repository.general.person.IPersonRepository
import br.com.fitnesspro.repository.general.user.ICustomUserRepository
import br.com.fitnesspro.repository.general.user.IUserRepository
import br.com.fitnesspro.services.firebase.FirebaseAuthenticationService
import br.com.fitnesspro.services.mappers.PersonServiceMapper
import br.com.fitnesspro.services.scheduler.SchedulerService
import br.com.fitnesspro.services.serviceauth.DeviceService
import br.com.fitnesspro.shared.communication.dtos.general.PersonDTO
import br.com.fitnesspro.shared.communication.dtos.general.UserDTO
import br.com.fitnesspro.shared.communication.dtos.scheduler.SchedulerConfigDTO
import br.com.fitnesspro.shared.communication.helper.HashHelper
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
    private val firebaseAuthenticationService: FirebaseAuthenticationService,
    private val personServiceMapper: PersonServiceMapper,
    private val deviceService: DeviceService
) {

    @CacheEvict(cacheNames = [PERSON_IMPORT_CACHE_NAME, PERSON_USER_IMPORT_CACHE_NAME], allEntries = true)
    fun savePerson(personDTO: PersonDTO) {
        val person = personServiceMapper.getPerson(personDTO)
        preparePersonSave(personDTO, person)

        userRepository.save(person.user!!)
        personRepository.save(person)

        if (personDTO.active) {
            val personExists = personDTO.id?.let { personRepository.findById(it).isPresent } ?: false
            firebaseAuthenticationService.saveUser(personDTO, personExists)
        } else {
            firebaseAuthenticationService.deleteUser(personDTO)
            deviceService.inactivatePersonDevice(person.id)
        }

        if (personDTO.createDefaultSchedulerConfig) {
            schedulerService.saveSchedulerConfig(SchedulerConfigDTO(personId = person.id))
        }

        personDTO.id = person.id
        personDTO.user?.id = person.user?.id
    }

    private fun preparePersonSave(personDTO: PersonDTO, person: Person) {
        if (personDTO.active) {
            validateUser(person.user!!)

            val password = person.user?.password!!

            if (!HashHelper.isHashed(password)) {
                person.user?.password = HashHelper.applyHash(password)
            }
        }
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
            val person = personServiceMapper.getPerson(personDTO)

            validateUser(person.user!!)

            val password = person.user?.password!!

            if (!HashHelper.isHashed(password)) {
                person.user?.password = HashHelper.applyHash(password)
            }

            if (personDTO.active) {
                val personExists = personDTO.id?.let { personRepository.findById(it).isPresent } ?: false
                firebaseAuthenticationService.saveUser(personDTO, personExists)
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
        return customPersonRepository.getPersonsImport(filter, pageInfos).map(personServiceMapper::getPersonDTO)
    }

    @Cacheable(cacheNames = [PERSON_USER_IMPORT_CACHE_NAME], key = "#filter.toCacheKey()")
    fun getUsersImport(filter: CommonImportFilter, pageInfos: ImportPageInfos): List<UserDTO> {
        return customPersonRepository.getUsersImport(filter, pageInfos).map(personServiceMapper::getUserDTO)
    }

    fun getListPersons(filter: PersonFilter, pageInfos: CommonPageInfos): List<PersonDTO> {
        return customPersonRepository.getListPersons(filter, pageInfos)
    }

    fun getCountListPersons(filter: PersonFilter): Int {
        return customPersonRepository.getCountListPersons(filter)
    }

    fun getPersonByEmail(email: String): PersonDTO? {
        return customPersonRepository.findByEmail(email)?.let(personServiceMapper::getPersonDTO)
    }
}