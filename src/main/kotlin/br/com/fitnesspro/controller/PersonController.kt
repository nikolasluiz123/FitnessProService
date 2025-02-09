package br.com.fitnesspro.controller

import br.com.fitnesspro.controller.constants.EndPoints
import br.com.fitnesspro.controller.constants.Timeouts
import br.com.fitnesspro.controller.responses.PersistenceServiceResponse
import br.com.fitnesspro.dto.PersonAcademyTimeDTO
import br.com.fitnesspro.dto.PersonDTO
import br.com.fitnesspro.service.AcademyService
import br.com.fitnesspro.service.PersonService
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

@RestController
@RequestMapping(EndPoints.PERSON_V1)
@Tag(name = "Person Controller", description = "Manutenção das Pessoas que acessam o app")
class PersonController(
    private val personService: PersonService,
    private val academyService: AcademyService
) {

    @PostMapping
    @Transactional(timeout = Timeouts.LOW_TIMEOUT)
    fun savePerson(@RequestBody @Valid personDTO: PersonDTO): ResponseEntity<PersistenceServiceResponse> {
        personService.savePerson(personDTO)
        return ResponseEntity.ok(PersistenceServiceResponse(code = HttpStatus.OK.value(), success = true))
    }

    @PostMapping(EndPoints.PERSON_BATCH)
    @Transactional(timeout = Timeouts.HIGH_TIMEOUT)
    @SecurityRequirement(name = "Bearer Authentication")
    fun savePersonList(@RequestBody @Valid personDTOList: List<PersonDTO>): ResponseEntity<PersistenceServiceResponse> {
        personService.savePersonList(personDTOList)
        return ResponseEntity.ok(PersistenceServiceResponse(code = HttpStatus.OK.value(), success = true))
    }

    @PostMapping(EndPoints.PERSON_ACADEMY_TIME)
    @Transactional(timeout = Timeouts.LOW_TIMEOUT)
    @SecurityRequirement(name = "Bearer Authentication")
    fun savePersonAcademyTime(@RequestBody @Valid personAcademyTimeDTO: PersonAcademyTimeDTO): ResponseEntity<PersistenceServiceResponse> {
        academyService.savePersonAcademyTime(personAcademyTimeDTO)
        return ResponseEntity.ok(PersistenceServiceResponse(code = HttpStatus.OK.value(), success = true))
    }
}