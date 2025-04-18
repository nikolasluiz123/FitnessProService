package br.com.fitnesspro.service.controller.general

import br.com.fitnesspro.service.exception.UserNotFoundException
import br.com.fitnesspro.service.service.general.UserService
import br.com.fitnesspro.shared.communication.constants.EndPointsV1
import br.com.fitnesspro.shared.communication.constants.Timeouts
import br.com.fitnesspro.shared.communication.dtos.general.AuthenticationDTO
import br.com.fitnesspro.shared.communication.responses.AuthenticationServiceResponse
import br.com.fitnesspro.shared.communication.responses.FitnessProServiceResponse
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
@RequestMapping(EndPointsV1.AUTHENTICATION_V1)
@Tag(name = "Authentication Controller", description = "Operações de Autenticação dos Usuários do App")
class AuthenticationController(
    private val userService: UserService
) {

    @PostMapping(EndPointsV1.AUTHENTICATION_LOGIN)
    @Transactional(timeout = Timeouts.OPERATION_LOW_TIMEOUT)
    @SecurityRequirement(name = "Bearer Authentication")
    fun login(@RequestBody @Valid authenticationDTO: AuthenticationDTO): ResponseEntity<AuthenticationServiceResponse> {
        return try {
            val token = userService.login(authenticationDTO)

            ResponseEntity.ok(
                AuthenticationServiceResponse(
                    code = HttpStatus.OK.value(),
                    success = true,
                    token = token
                )
            )
        } catch (exception: UserNotFoundException) {
            ResponseEntity.ok(
                AuthenticationServiceResponse(
                    code = HttpStatus.NOT_FOUND.value(),
                    success = false,
                    error = "Credenciais inválidas, por favor, redigite."
                )
            )
        }
    }

    @PostMapping(EndPointsV1.AUTHENTICATION_LOGOUT)
    @Transactional(timeout = Timeouts.OPERATION_LOW_TIMEOUT)
    @SecurityRequirement(name = "Bearer Authentication")
    fun logout(@RequestBody @Valid authenticationDTO: AuthenticationDTO): ResponseEntity<FitnessProServiceResponse> {
        userService.logout(authenticationDTO)
        return ResponseEntity.ok(FitnessProServiceResponse(code = HttpStatus.OK.value(), success = true))
    }
}