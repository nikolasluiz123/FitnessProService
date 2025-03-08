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
import br.com.fitnesspro.shared.communication.responses.ExportationServiceResponse
import br.com.fitnesspro.shared.communication.responses.ImportationServiceResponse
import br.com.fitnesspro.shared.communication.responses.PersistenceServiceResponse
import com.google.gson.GsonBuilder
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
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
    fun savePersonBatch(@RequestBody @Valid personDTOList: List<PersonDTO>, request: HttpServletRequest): ResponseEntity<ExportationServiceResponse> {
        personService.savePersonList(personDTOList)

        val logId = request.getAttribute("logId") as String
        return ResponseEntity.ok(ExportationServiceResponse(executionLogId = logId, code = HttpStatus.OK.value(), success = true))
    }

    @PostMapping(EndPointsV1.PERSON_MOCK)
    @Transactional(timeout = Timeouts.OPERATION_HIGH_TIMEOUT)
    @SecurityRequirement(name = "Bearer Authentication")
    fun savePersonMock(): ResponseEntity<PersistenceServiceResponse> {
        personService.savePersonMock()

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
    fun savePersonAcademyTimeBatch(@RequestBody @Valid personAcademyTimeDTOList: List<PersonAcademyTimeDTO>, request: HttpServletRequest): ResponseEntity<ExportationServiceResponse> {
        academyService.savePersonAcademyTimeBatch(personAcademyTimeDTOList)

        val logId = request.getAttribute("logId") as String
        return ResponseEntity.ok(ExportationServiceResponse(executionLogId = logId, code = HttpStatus.OK.value(), success = true))
    }

    @GetMapping(EndPointsV1.PERSON_IMPORT)
    @Transactional(timeout = Timeouts.OPERATION_MEDIUM_TIMEOUT)
    @SecurityRequirement(name = "Bearer Authentication")
    fun importPersons(@RequestParam filter: String, @RequestParam pageInfos: String, request: HttpServletRequest): ResponseEntity<ImportationServiceResponse<PersonDTO>> {
        val defaultGSon = GsonBuilder().defaultGSon()
        val queryFilter = defaultGSon.fromJson(filter, CommonImportFilter::class.java)
        val commonPageInfos = defaultGSon.fromJson(pageInfos, ImportPageInfos::class.java)

        val values = personService.getPersonsImport(queryFilter, commonPageInfos)
        val logId = request.getAttribute("logId") as String
        return ResponseEntity.ok(ImportationServiceResponse(executionLogId = logId, values = values, code = HttpStatus.OK.value(), success = true))
    }

    @GetMapping(EndPointsV1.PERSON_USER_IMPORT)
    @Transactional(timeout = Timeouts.OPERATION_MEDIUM_TIMEOUT)
    @SecurityRequirement(name = "Bearer Authentication")
    fun importUsers(@RequestParam filter: String, @RequestParam pageInfos: String, request: HttpServletRequest): ResponseEntity<ImportationServiceResponse<UserDTO>> {
        val defaultGSon = GsonBuilder().defaultGSon()
        val queryFilter = defaultGSon.fromJson(filter, CommonImportFilter::class.java)
        val commonPageInfos = defaultGSon.fromJson(pageInfos, ImportPageInfos::class.java)

        val values = personService.getUsersImport(queryFilter, commonPageInfos)
        val logId = request.getAttribute("logId") as String
        return ResponseEntity.ok(ImportationServiceResponse(executionLogId = logId, values = values, code = HttpStatus.OK.value(), success = true))
    }

    @GetMapping(EndPointsV1.PERSON_ACADEMY_TIME_IMPORT)
    @Transactional(timeout = Timeouts.OPERATION_MEDIUM_TIMEOUT)
    @SecurityRequirement(name = "Bearer Authentication")
    fun importPersonAcademyTime(@RequestParam filter: String, @RequestParam pageInfos: String, request: HttpServletRequest): ResponseEntity<ImportationServiceResponse<PersonAcademyTimeDTO>> {
        val defaultGSon = GsonBuilder().defaultGSon()
        val queryFilter = defaultGSon.fromJson(filter, CommonImportFilter::class.java)
        val commonPageInfos = defaultGSon.fromJson(pageInfos, ImportPageInfos::class.java)

        val values = academyService.getPersonAcademyTimesImport(queryFilter, commonPageInfos)
        val logId = request.getAttribute("logId") as String
        return ResponseEntity.ok(ImportationServiceResponse(executionLogId = logId, values = values, code = HttpStatus.OK.value(), success = true))
    }

}