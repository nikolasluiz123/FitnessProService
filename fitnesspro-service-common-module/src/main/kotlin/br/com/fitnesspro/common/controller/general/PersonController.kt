package br.com.fitnesspro.common.controller.general

import br.com.fitnesspro.authentication.service.PersonService
import br.com.fitnesspro.common.service.general.AcademyService
import br.com.fitnesspro.log.enums.EnumRequestAttributes
import br.com.fitnesspro.service.communication.dtos.general.ValidatedFindPersonDTO
import br.com.fitnesspro.service.communication.dtos.general.ValidatedPersonAcademyTimeDTO
import br.com.fitnesspro.service.communication.dtos.general.ValidatedPersonDTO
import br.com.fitnesspro.service.communication.dtos.general.ValidatedUserDTO
import br.com.fitnesspro.service.communication.gson.defaultServiceGSon
import br.com.fitnesspro.service.communication.responses.*
import br.com.fitnesspro.shared.communication.constants.EndPointsV1
import br.com.fitnesspro.shared.communication.constants.Timeouts
import br.com.fitnesspro.shared.communication.paging.CommonPageInfos
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.PersonFilter
import br.com.fitnesspro.shared.communication.query.filter.importation.CommonImportFilter
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
    fun savePerson(@RequestBody @Valid validatedPersonDTO: ValidatedPersonDTO): ResponseEntity<ValidatedPersistenceServiceResponse<ValidatedPersonDTO>> {
        personService.savePerson(validatedPersonDTO)
        return ResponseEntity.ok(
            ValidatedPersistenceServiceResponse(
                code = HttpStatus.OK.value(),
                success = true,
                savedDTO = validatedPersonDTO
            )
        )
    }

    @PostMapping(EndPointsV1.PERSON_EXPORT)
    @Transactional(timeout = Timeouts.OPERATION_HIGH_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun savePersonBatch(@RequestBody @Valid validatedPersonDTOList: List<ValidatedPersonDTO>, request: HttpServletRequest): ResponseEntity<ValidatedExportationServiceResponse> {
        personService.savePersonList(validatedPersonDTOList)

        val logId = request.getAttribute(EnumRequestAttributes.LOG_ID.name) as String
        val logPackageId = request.getAttribute(EnumRequestAttributes.LOG_PACKAGE_ID.name) as String
        return ResponseEntity.ok(
            ValidatedExportationServiceResponse(
                executionLogId = logId,
                executionLogPackageId = logPackageId,
                code = HttpStatus.OK.value(),
                success = true
            )
        )
    }

    @PostMapping(EndPointsV1.PERSON_ACADEMY_TIME_EXPORT)
    @Transactional(timeout = Timeouts.OPERATION_HIGH_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun savePersonAcademyTimeBatch(@RequestBody @Valid validatedPersonAcademyTimeDTOList: List<ValidatedPersonAcademyTimeDTO>, request: HttpServletRequest): ResponseEntity<ValidatedExportationServiceResponse> {
        academyService.savePersonAcademyTimeBatch(validatedPersonAcademyTimeDTOList)

        val logId = request.getAttribute(EnumRequestAttributes.LOG_ID.name) as String
        val logPackageId = request.getAttribute(EnumRequestAttributes.LOG_PACKAGE_ID.name) as String
        return ResponseEntity.ok(
            ValidatedExportationServiceResponse(
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
    fun importPersons(@RequestParam filter: String, @RequestParam pageInfos: String, request: HttpServletRequest): ResponseEntity<ValidatedImportationServiceResponse<ValidatedPersonDTO>> {
        val defaultGSon = GsonBuilder().defaultServiceGSon()
        val queryFilter = defaultGSon.fromJson(filter, CommonImportFilter::class.java)
        val commonPageInfos = defaultGSon.fromJson(pageInfos, ImportPageInfos::class.java)

        val values = personService.getPersonsImport(queryFilter, commonPageInfos)
        val logId = request.getAttribute(EnumRequestAttributes.LOG_ID.name) as String
        val logPackageId = request.getAttribute(EnumRequestAttributes.LOG_PACKAGE_ID.name) as String
        return ResponseEntity.ok(
            ValidatedImportationServiceResponse(
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
    fun importUsers(@RequestParam filter: String, @RequestParam pageInfos: String, request: HttpServletRequest): ResponseEntity<ValidatedImportationServiceResponse<ValidatedUserDTO>> {
        val defaultGSon = GsonBuilder().defaultServiceGSon()
        val queryFilter = defaultGSon.fromJson(filter, CommonImportFilter::class.java)
        val commonPageInfos = defaultGSon.fromJson(pageInfos, ImportPageInfos::class.java)

        val values = personService.getUsersImport(queryFilter, commonPageInfos)
        val logId = request.getAttribute(EnumRequestAttributes.LOG_ID.name) as String
        val logPackageId = request.getAttribute(EnumRequestAttributes.LOG_PACKAGE_ID.name) as String
        return ResponseEntity.ok(
            ValidatedImportationServiceResponse(
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
    fun importPersonAcademyTime(@RequestParam filter: String, @RequestParam pageInfos: String, request: HttpServletRequest): ResponseEntity<ValidatedImportationServiceResponse<ValidatedPersonAcademyTimeDTO>> {
        val defaultGSon = GsonBuilder().defaultServiceGSon()
        val queryFilter = defaultGSon.fromJson(filter, CommonImportFilter::class.java)
        val commonPageInfos = defaultGSon.fromJson(pageInfos, ImportPageInfos::class.java)

        val values = academyService.getPersonAcademyTimesImport(queryFilter, commonPageInfos)
        val logId = request.getAttribute(EnumRequestAttributes.LOG_ID.name) as String
        val logPackageId = request.getAttribute(EnumRequestAttributes.LOG_PACKAGE_ID.name) as String
        return ResponseEntity.ok(
            ValidatedImportationServiceResponse(
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
    fun getPersonByEmail(@RequestBody dto: ValidatedFindPersonDTO): ResponseEntity<ValidatedSingleValueServiceResponse<ValidatedPersonDTO?>> {
        val person = personService.getPersonByEmail(dto.email!!, dto.password)
        return ResponseEntity.ok(
            ValidatedSingleValueServiceResponse(
                value = person,
                code = HttpStatus.OK.value(),
                success = true
            )
        )
    }

    @GetMapping(EndPointsV1.PERSON_LIST)
    @Transactional(timeout = Timeouts.OPERATION_LOW_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun getListPerson(@RequestParam filter: String, @RequestParam pageInfos: String): ResponseEntity<ValidatedReadServiceResponse<ValidatedPersonDTO>> {
        val defaultGSon = GsonBuilder().defaultServiceGSon()
        val queryFilter = defaultGSon.fromJson(filter, PersonFilter::class.java)
        val commonPageInfos = defaultGSon.fromJson(pageInfos, CommonPageInfos::class.java)

        val persons = personService.getListPersons(filter = queryFilter, pageInfos = commonPageInfos)
        return ResponseEntity.ok(
            ValidatedReadServiceResponse(
                values = persons,
                code = HttpStatus.OK.value(),
                success = true
            )
        )
    }

    @GetMapping(EndPointsV1.PERSON_COUNT)
    @Transactional(timeout = Timeouts.OPERATION_LOW_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun getCountListPerson(@RequestParam filter: String): ResponseEntity<ValidatedSingleValueServiceResponse<Int>> {
        val defaultGSon = GsonBuilder().defaultServiceGSon()
        val queryFilter = defaultGSon.fromJson(filter, PersonFilter::class.java)

        val count = personService.getCountListPersons(filter = queryFilter)
        return ResponseEntity.ok(
            ValidatedSingleValueServiceResponse(
                value = count,
                code = HttpStatus.OK.value(),
                success = true
            )
        )
    }

}