package br.com.fitnesspro.service.controller.general

import br.com.fitnesspro.service.service.general.AcademyService
import br.com.fitnesspro.service.service.general.PersonService
import br.com.fitnesspro.shared.communication.constants.EndPointsV1
import br.com.fitnesspro.shared.communication.constants.Timeouts
import br.com.fitnesspro.shared.communication.dtos.general.PersonAcademyTimeDTO
import br.com.fitnesspro.shared.communication.dtos.general.PersonDTO
import br.com.fitnesspro.shared.communication.filter.CommonImportFilter
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.responses.PersistenceServiceResponse
import br.com.fitnesspro.shared.communication.responses.ReadServiceResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

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
        return ResponseEntity.ok(PersistenceServiceResponse(code = HttpStatus.OK.value(), success = true, transmissionDate = LocalDateTime.now()))
    }

    @PostMapping(EndPointsV1.PERSON_EXPORT)
    @Transactional(timeout = Timeouts.OPERATION_HIGH_TIMEOUT)
    @SecurityRequirement(name = "Bearer Authentication")
    fun savePersonBatch(@RequestBody @Valid personDTOList: List<PersonDTO>): ResponseEntity<PersistenceServiceResponse> {
        personService.savePersonList(personDTOList)
        return ResponseEntity.ok(PersistenceServiceResponse(code = HttpStatus.OK.value(), success = true, transmissionDate = LocalDateTime.now()))
    }

    @PostMapping(EndPointsV1.PERSON_ACADEMY_TIME)
    @Transactional(timeout = Timeouts.OPERATION_LOW_TIMEOUT)
    @SecurityRequirement(name = "Bearer Authentication")
    fun savePersonAcademyTime(@RequestBody @Valid personAcademyTimeDTO: PersonAcademyTimeDTO): ResponseEntity<PersistenceServiceResponse> {
        academyService.savePersonAcademyTime(personAcademyTimeDTO)
        return ResponseEntity.ok(PersistenceServiceResponse(code = HttpStatus.OK.value(), success = true, transmissionDate = LocalDateTime.now()))
    }

    @PostMapping(EndPointsV1.PERSON_ACADEMY_TIME_EXPORT)
    @Transactional(timeout = Timeouts.OPERATION_HIGH_TIMEOUT)
    @SecurityRequirement(name = "Bearer Authentication")
    fun savePersonAcademyTimeBatch(@RequestBody @Valid personAcademyTimeDTOList: List<PersonAcademyTimeDTO>): ResponseEntity<PersistenceServiceResponse> {
        academyService.savePersonAcademyTimeBatch(personAcademyTimeDTOList)
        return ResponseEntity.ok(PersistenceServiceResponse(code = HttpStatus.OK.value(), success = true, transmissionDate = LocalDateTime.now()))
    }

    @PostMapping(EndPointsV1.PERSON_IMPORT)
    @Transactional(timeout = Timeouts.OPERATION_MEDIUM_TIMEOUT)
    @SecurityRequirement(name = "Bearer Authentication")
    fun importPersons(@RequestBody filter: CommonImportFilter, pageInfos: ImportPageInfos): ResponseEntity<ReadServiceResponse<PersonDTO>> {
        val users = personService.getPersonsImport(filter, pageInfos)
        return ResponseEntity.ok(ReadServiceResponse(values = users, code = HttpStatus.OK.value(), success = true))
    }

    @PostMapping(EndPointsV1.PERSON_ACADEMY_TIME_IMPORT)
    @Transactional(timeout = Timeouts.OPERATION_MEDIUM_TIMEOUT)
    @SecurityRequirement(name = "Bearer Authentication")
    fun importPersonAcademyTime(@RequestBody filter: CommonImportFilter, pageInfos: ImportPageInfos): ResponseEntity<ReadServiceResponse<PersonAcademyTimeDTO>> {
        val users = academyService.getPersonAcademyTimesImport(filter, pageInfos)
        return ResponseEntity.ok(ReadServiceResponse(values = users, code = HttpStatus.OK.value(), success = true))
    }

}