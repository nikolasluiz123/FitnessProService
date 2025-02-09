package br.com.fitnesspro.controller

import br.com.fitnesspro.controller.constants.EndPoints
import br.com.fitnesspro.controller.constants.Timeouts
import br.com.fitnesspro.controller.responses.AuthenticationServiceResponse
import br.com.fitnesspro.controller.responses.FitnessProServiceResponse
import br.com.fitnesspro.dto.AuthenticationDTO
import br.com.fitnesspro.service.UserService
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
@RequestMapping(EndPoints.AUTHENTICATION_V1)
@Tag(name = "Authentication Controller", description = "Operações de Autenticação dos Usuários do App")
class AuthenticationController(
    private val userService: UserService
) {

    @PostMapping(EndPoints.AUTHENTICATION_LOGIN)
    @Transactional(timeout = Timeouts.LOW_TIMEOUT)
    fun login(@RequestBody @Valid authenticationDTO: AuthenticationDTO): ResponseEntity<AuthenticationServiceResponse> {
        val token = userService.login(authenticationDTO)

        return ResponseEntity.ok(
            AuthenticationServiceResponse(
                code = HttpStatus.OK.value(),
                success = true,
                token = token
            )
        )
    }

    @PostMapping(EndPoints.AUTHENTICATION_LOGOUT)
    @Transactional(timeout = Timeouts.LOW_TIMEOUT)
    @SecurityRequirement(name = "Bearer Authentication")
    fun logout(@RequestBody @Valid authenticationDTO: AuthenticationDTO): ResponseEntity<FitnessProServiceResponse> {
        userService.logout(authenticationDTO)
        return ResponseEntity.ok(FitnessProServiceResponse(code = HttpStatus.OK.value(), success = true))
    }
}