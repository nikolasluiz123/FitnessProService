package br.com.fitnesspro.authentication.service

import br.com.fitnesspro.authentication.repository.auditable.IPersonRepository
import br.com.fitnesspro.authentication.repository.auditable.IUserRepository
import br.com.fitnesspro.authentication.repository.jpa.ICustomPersonRepository
import br.com.fitnesspro.authentication.repository.jpa.ICustomUserRepository
import br.com.fitnesspro.authentication.service.mappers.PersonServiceMapper
import br.com.fitnesspro.authentication.service.mappers.UserServiceMapper
import br.com.fitnesspro.core.cache.PERSON_IMPORT_CACHE_NAME
import br.com.fitnesspro.core.cache.PERSON_USER_IMPORT_CACHE_NAME
import br.com.fitnesspro.core.exceptions.BusinessException
import br.com.fitnesspro.models.general.Person
import br.com.fitnesspro.models.general.User
import br.com.fitnesspro.service.communication.dtos.general.ValidatedPersonDTO
import br.com.fitnesspro.service.communication.dtos.general.ValidatedUserDTO
import br.com.fitnesspro.service.communication.dtos.scheduler.ValidatedSchedulerConfigDTO
import br.com.fitnesspro.shared.communication.helper.HashHelper
import br.com.fitnesspro.shared.communication.paging.CommonPageInfos
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.PersonFilter
import br.com.fitnesspro.shared.communication.query.filter.importation.CommonImportFilter
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.context.MessageSource
import org.springframework.stereotype.Service
import java.util.*

@Service
class PersonService(
    private val userRepository: IUserRepository,
    private val personRepository: IPersonRepository,
    private val customUserRepository: ICustomUserRepository,
    private val customPersonRepository: ICustomPersonRepository,
    private val schedulerConfigService: SchedulerConfigService,
    private val firebaseAuthenticationService: FirebaseAuthenticationService,
    private val personServiceMapper: PersonServiceMapper,
    private val userServiceMapper: UserServiceMapper,
    private val deviceService: DeviceService,
    private val messageSource: MessageSource
) {

    @CacheEvict(cacheNames = [PERSON_IMPORT_CACHE_NAME, PERSON_USER_IMPORT_CACHE_NAME], allEntries = true)
    fun savePerson(personDTO: ValidatedPersonDTO) {
        val person = personServiceMapper.getPerson(personDTO)
        preparePersonSave(personDTO, person)

        val personExists = personDTO.id?.let { personRepository.findById(it).isPresent } ?: false

        userRepository.save(person.user!!)
        personRepository.save(person)

        if (personDTO.active) {
            firebaseAuthenticationService.saveUser(personDTO, personExists)
        } else {
            firebaseAuthenticationService.deleteUser(personDTO)
            deviceService.inactivatePersonDevice(person.id)
        }

        if (personDTO.createDefaultSchedulerConfig) {
            schedulerConfigService.saveSchedulerConfig(ValidatedSchedulerConfigDTO(personId = person.id))
        }

        personDTO.id = person.id
        personDTO.user?.id = person.user?.id
    }

    private fun preparePersonSave(personDTO: ValidatedPersonDTO, person: Person) {
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
            val message = messageSource.getMessage("person.error.email.in.use", null, Locale.getDefault())
            throw BusinessException(message)
        }

        if (user.password.length < 6) {
            val message = messageSource.getMessage("person.error.password.short", null, Locale.getDefault())
            throw BusinessException(message)
        }
    }

    @CacheEvict(cacheNames = [PERSON_IMPORT_CACHE_NAME, PERSON_USER_IMPORT_CACHE_NAME], allEntries = true)
    fun savePersonList(personDTOList: List<ValidatedPersonDTO>) {
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
    fun getPersonsImport(filter: CommonImportFilter, pageInfos: ImportPageInfos): List<ValidatedPersonDTO> {
        return customPersonRepository.getPersonsImport(filter, pageInfos).map(personServiceMapper::getPersonDTO)
    }

    @Cacheable(cacheNames = [PERSON_USER_IMPORT_CACHE_NAME], key = "#filter.toCacheKey()")
    fun getUsersImport(filter: CommonImportFilter, pageInfos: ImportPageInfos): List<ValidatedUserDTO> {
        return customPersonRepository.getUsersImport(filter, pageInfos).map(userServiceMapper::getUserDTO)
    }

    fun getListPersons(filter: PersonFilter, pageInfos: CommonPageInfos): List<ValidatedPersonDTO> {
        return customPersonRepository.getListPersons(filter, pageInfos)
    }

    fun getCountListPersons(filter: PersonFilter): Int {
        return customPersonRepository.getCountListPersons(filter)
    }

    fun getPersonByEmail(email: String, password: String?): ValidatedPersonDTO? {
        return customPersonRepository.findByEmail(email, password)?.let(personServiceMapper::getPersonDTO)
    }
}