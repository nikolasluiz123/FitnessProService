package br.com.fitnesspro.controller.general

import br.com.fitnesspro.config.gson.defaultGSon
import br.com.fitnesspro.config.request.EnumRequestAttributes
import br.com.fitnesspro.services.general.AcademyService
import br.com.fitnesspro.services.general.PersonService
import br.com.fitnesspro.shared.communication.constants.EndPointsV1
import br.com.fitnesspro.shared.communication.constants.Timeouts
import br.com.fitnesspro.shared.communication.dtos.general.FindPersonDTO
import br.com.fitnesspro.shared.communication.dtos.general.PersonAcademyTimeDTO
import br.com.fitnesspro.shared.communication.dtos.general.PersonDTO
import br.com.fitnesspro.shared.communication.dtos.general.UserDTO
import br.com.fitnesspro.shared.communication.paging.CommonPageInfos
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.PersonFilter
import br.com.fitnesspro.shared.communication.query.filter.importation.CommonImportFilter
import br.com.fitnesspro.shared.communication.responses.*
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
    @Transactional(timeout = Timeouts.OPERATION_LOW_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun savePerson(@RequestBody @Valid personDTO: PersonDTO): ResponseEntity<PersistenceServiceResponse<PersonDTO>> {
        personService.savePerson(personDTO)
        return ResponseEntity.ok(PersistenceServiceResponse(code = HttpStatus.OK.value(), success = true, savedDTO = personDTO))
    }

    @PostMapping(EndPointsV1.PERSON_EXPORT)
    @Transactional(timeout = Timeouts.OPERATION_HIGH_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun savePersonBatch(@RequestBody @Valid personDTOList: List<PersonDTO>, request: HttpServletRequest): ResponseEntity<ExportationServiceResponse> {
        personService.savePersonList(personDTOList)

        val logId = request.getAttribute(EnumRequestAttributes.LOG_ID.name) as String
        val logPackageId = request.getAttribute(EnumRequestAttributes.LOG_PACKAGE_ID.name) as String
        return ResponseEntity.ok(
            ExportationServiceResponse(
                executionLogId = logId,
                executionLogPackageId = logPackageId,
                code = HttpStatus.OK.value(),
                success = true
            )
        )
    }

    @PostMapping(EndPointsV1.PERSON_ACADEMY_TIME)
    @Transactional(timeout = Timeouts.OPERATION_LOW_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun savePersonAcademyTime(@RequestBody @Valid personAcademyTimeDTO: PersonAcademyTimeDTO): ResponseEntity<PersistenceServiceResponse<PersonAcademyTimeDTO>> {
        academyService.savePersonAcademyTime(personAcademyTimeDTO)
        return ResponseEntity.ok(PersistenceServiceResponse(code = HttpStatus.OK.value(), success = true, savedDTO = personAcademyTimeDTO))
    }

    @PostMapping(EndPointsV1.PERSON_ACADEMY_TIME_EXPORT)
    @Transactional(timeout = Timeouts.OPERATION_HIGH_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun savePersonAcademyTimeBatch(@RequestBody @Valid personAcademyTimeDTOList: List<PersonAcademyTimeDTO>, request: HttpServletRequest): ResponseEntity<ExportationServiceResponse> {
        academyService.savePersonAcademyTimeBatch(personAcademyTimeDTOList)

        val logId = request.getAttribute(EnumRequestAttributes.LOG_ID.name) as String
        val logPackageId = request.getAttribute(EnumRequestAttributes.LOG_PACKAGE_ID.name) as String
        return ResponseEntity.ok(
            ExportationServiceResponse(
                executionLogId = logId,
                executionLogPackageId = logPackageId,
                code = HttpStatus.OK.value(),
                success = true
            )
        )
    }

    @GetMapping(EndPointsV1.PERSON_IMPORT)
    @Transactional(timeout = Timeouts.OPERATION_MEDIUM_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun importPersons(@RequestParam filter: String, @RequestParam pageInfos: String, request: HttpServletRequest): ResponseEntity<ImportationServiceResponse<PersonDTO>> {
        val defaultGSon = GsonBuilder().defaultGSon()
        val queryFilter = defaultGSon.fromJson(filter, CommonImportFilter::class.java)
        val commonPageInfos = defaultGSon.fromJson(pageInfos, ImportPageInfos::class.java)

        val values = personService.getPersonsImport(queryFilter, commonPageInfos)
        val logId = request.getAttribute(EnumRequestAttributes.LOG_ID.name) as String
        val logPackageId = request.getAttribute(EnumRequestAttributes.LOG_PACKAGE_ID.name) as String
        return ResponseEntity.ok(
            ImportationServiceResponse(
                executionLogId = logId,
                executionLogPackageId = logPackageId,
                values = values,
                code = HttpStatus.OK.value(),
                success = true,
            )
        )
    }

    @GetMapping(EndPointsV1.PERSON_USER_IMPORT)
    @Transactional(timeout = Timeouts.OPERATION_MEDIUM_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun importUsers(@RequestParam filter: String, @RequestParam pageInfos: String, request: HttpServletRequest): ResponseEntity<ImportationServiceResponse<UserDTO>> {
        val defaultGSon = GsonBuilder().defaultGSon()
        val queryFilter = defaultGSon.fromJson(filter, CommonImportFilter::class.java)
        val commonPageInfos = defaultGSon.fromJson(pageInfos, ImportPageInfos::class.java)

        val values = personService.getUsersImport(queryFilter, commonPageInfos)
        val logId = request.getAttribute(EnumRequestAttributes.LOG_ID.name) as String
        val logPackageId = request.getAttribute(EnumRequestAttributes.LOG_PACKAGE_ID.name) as String
        return ResponseEntity.ok(
            ImportationServiceResponse(
                executionLogId = logId,
                executionLogPackageId = logPackageId,
                values = values,
                code = HttpStatus.OK.value(),
                success = true,
            )
        )
    }

    @GetMapping(EndPointsV1.PERSON_ACADEMY_TIME_IMPORT)
    @Transactional(timeout = Timeouts.OPERATION_MEDIUM_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun importPersonAcademyTime(@RequestParam filter: String, @RequestParam pageInfos: String, request: HttpServletRequest): ResponseEntity<ImportationServiceResponse<PersonAcademyTimeDTO>> {
        val defaultGSon = GsonBuilder().defaultGSon()
        val queryFilter = defaultGSon.fromJson(filter, CommonImportFilter::class.java)
        val commonPageInfos = defaultGSon.fromJson(pageInfos, ImportPageInfos::class.java)

        val values = academyService.getPersonAcademyTimesImport(queryFilter, commonPageInfos)
        val logId = request.getAttribute(EnumRequestAttributes.LOG_ID.name) as String
        val logPackageId = request.getAttribute(EnumRequestAttributes.LOG_PACKAGE_ID.name) as String
        return ResponseEntity.ok(
            ImportationServiceResponse(
                executionLogId = logId,
                executionLogPackageId = logPackageId,
                values = values,
                code = HttpStatus.OK.value(),
                success = true,
            )
        )
    }

    @PostMapping(EndPointsV1.PERSON_EMAIL)
    @Transactional(timeout = Timeouts.OPERATION_LOW_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun getPersonByEmail(@RequestBody dto: FindPersonDTO): ResponseEntity<SingleValueServiceResponse<PersonDTO?>> {
        val person = personService.getPersonByEmail(dto.email!!, dto.password)
        return ResponseEntity.ok(SingleValueServiceResponse(value = person, code = HttpStatus.OK.value(), success = true))
    }

    @GetMapping(EndPointsV1.PERSON_LIST)
    @Transactional(timeout = Timeouts.OPERATION_LOW_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun getListPerson(@RequestParam filter: String, @RequestParam pageInfos: String): ResponseEntity<ReadServiceResponse<PersonDTO>> {
        val defaultGSon = GsonBuilder().defaultGSon()
        val queryFilter = defaultGSon.fromJson(filter, PersonFilter::class.java)
        val commonPageInfos = defaultGSon.fromJson(pageInfos, CommonPageInfos::class.java)

        val persons = personService.getListPersons(filter = queryFilter, pageInfos = commonPageInfos)
        return ResponseEntity.ok(ReadServiceResponse(values = persons, code = HttpStatus.OK.value(), success = true))
    }

    @GetMapping(EndPointsV1.PERSON_COUNT)
    @Transactional(timeout = Timeouts.OPERATION_LOW_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun getCountListPerson(@RequestParam filter: String): ResponseEntity<SingleValueServiceResponse<Int>> {
        val defaultGSon = GsonBuilder().defaultGSon()
        val queryFilter = defaultGSon.fromJson(filter, PersonFilter::class.java)

        val count = personService.getCountListPersons(filter = queryFilter)
        return ResponseEntity.ok(SingleValueServiceResponse(value = count, code = HttpStatus.OK.value(), success = true))
    }

}