package br.com.fitnesspro.service.controller.general

import br.com.fitnesspro.service.config.gson.defaultGSon
import br.com.fitnesspro.service.service.general.AcademyService
import br.com.fitnesspro.service.service.general.PersonService
import br.com.fitnesspro.shared.communication.constants.EndPointsV1
import br.com.fitnesspro.shared.communication.constants.Timeouts
import br.com.fitnesspro.shared.communication.dtos.general.PersonAcademyTimeDTO
import br.com.fitnesspro.shared.communication.dtos.general.PersonDTO
import br.com.fitnesspro.shared.communication.dtos.general.UserDTO
import br.com.fitnesspro.shared.communication.filter.CommonImportFilter
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.responses.PersistenceServiceResponse
import br.com.fitnesspro.shared.communication.responses.ReadServiceResponse
import com.google.gson.GsonBuilder
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(EndPointsV1.PERSON_V1)
@Tag(name = "Person Controller", description = "Manutenção das Pessoas que acessam o app")
class PersonController(
    private val personService: PersonService,
    private val academyService: AcademyService
) {

    @PostMapping
    @Transactional(timeout = Timeouts.OPERATION_LOW_TIMEOUT)
    fun savePerson(@RequestBody @Valid personDTO: PersonDTO): ResponseEntity<PersistenceServiceResponse> {
        personService.savePerson(personDTO)
        return ResponseEntity.ok(PersistenceServiceResponse(code = HttpStatus.OK.value(), success = true))
    }

    @PostMapping(EndPointsV1.PERSON_EXPORT)
    @Transactional(timeout = Timeouts.OPERATION_HIGH_TIMEOUT)
    @SecurityRequirement(name = "Bearer Authentication")
    fun savePersonBatch(@RequestBody @Valid personDTOList: List<PersonDTO>): ResponseEntity<PersistenceServiceResponse> {
        personService.savePersonList(personDTOList)
        return ResponseEntity.ok(PersistenceServiceResponse(code = HttpStatus.OK.value(), success = true))
    }

    @PostMapping(EndPointsV1.PERSON_ACADEMY_TIME)
    @Transactional(timeout = Timeouts.OPERATION_LOW_TIMEOUT)
    @SecurityRequirement(name = "Bearer Authentication")
    fun savePersonAcademyTime(@RequestBody @Valid personAcademyTimeDTO: PersonAcademyTimeDTO): ResponseEntity<PersistenceServiceResponse> {
        academyService.savePersonAcademyTime(personAcademyTimeDTO)
        return ResponseEntity.ok(PersistenceServiceResponse(code = HttpStatus.OK.value(), success = true))
    }

    @PostMapping(EndPointsV1.PERSON_ACADEMY_TIME_EXPORT)
    @Transactional(timeout = Timeouts.OPERATION_HIGH_TIMEOUT)
    @SecurityRequirement(name = "Bearer Authentication")
    fun savePersonAcademyTimeBatch(@RequestBody @Valid personAcademyTimeDTOList: List<PersonAcademyTimeDTO>): ResponseEntity<PersistenceServiceResponse> {
        academyService.savePersonAcademyTimeBatch(personAcademyTimeDTOList)
        return ResponseEntity.ok(PersistenceServiceResponse(code = HttpStatus.OK.value(), success = true))
    }

    @GetMapping(EndPointsV1.PERSON_IMPORT)
    @Transactional(timeout = Timeouts.OPERATION_MEDIUM_TIMEOUT)
    @SecurityRequirement(name = "Bearer Authentication")
    fun importPersons(@RequestParam filter: String, @RequestParam pageInfos: String): ResponseEntity<ReadServiceResponse<PersonDTO>> {
        val defaultGSon = GsonBuilder().defaultGSon()
        val queryFilter = defaultGSon.fromJson(filter, CommonImportFilter::class.java)
        val commonPageInfos = defaultGSon.fromJson(filter, ImportPageInfos::class.java)

        val users = personService.getPersonsImport(queryFilter, commonPageInfos)
        return ResponseEntity.ok(ReadServiceResponse(values = users, code = HttpStatus.OK.value(), success = true))
    }

    @GetMapping(EndPointsV1.PERSON_USER_IMPORT)
    @Transactional(timeout = Timeouts.OPERATION_MEDIUM_TIMEOUT)
    @SecurityRequirement(name = "Bearer Authentication")
    fun importUsers(@RequestParam filter: String, @RequestParam pageInfos: String): ResponseEntity<ReadServiceResponse<UserDTO>> {
        val defaultGSon = GsonBuilder().defaultGSon()
        val queryFilter = defaultGSon.fromJson(filter, CommonImportFilter::class.java)
        val commonPageInfos = defaultGSon.fromJson(filter, ImportPageInfos::class.java)

        val users = personService.getUsersImport(queryFilter, commonPageInfos)
        return ResponseEntity.ok(ReadServiceResponse(values = users, code = HttpStatus.OK.value(), success = true))
    }

    @GetMapping(EndPointsV1.PERSON_ACADEMY_TIME_IMPORT)
    @Transactional(timeout = Timeouts.OPERATION_MEDIUM_TIMEOUT)
    @SecurityRequirement(name = "Bearer Authentication")
    fun importPersonAcademyTime(@RequestParam filter: String, @RequestParam pageInfos: String): ResponseEntity<ReadServiceResponse<PersonAcademyTimeDTO>> {
        val defaultGSon = GsonBuilder().defaultGSon()
        val queryFilter = defaultGSon.fromJson(filter, CommonImportFilter::class.java)
        val commonPageInfos = defaultGSon.fromJson(filter, ImportPageInfos::class.java)

        val users = academyService.getPersonAcademyTimesImport(queryFilter, commonPageInfos)
        return ResponseEntity.ok(ReadServiceResponse(values = users, code = HttpStatus.OK.value(), success = true))
    }

}