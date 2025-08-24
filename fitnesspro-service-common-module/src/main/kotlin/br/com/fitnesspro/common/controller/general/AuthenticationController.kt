package br.com.fitnesspro.common.controller.general

import br.com.fitnesspro.authentication.service.UserService
import br.com.fitnesspro.service.communication.dtos.general.ValidatedAuthenticationDTO
import br.com.fitnesspro.service.communication.responses.ValidatedAuthenticationServiceResponse
import br.com.fitnesspro.shared.communication.constants.EndPointsV1
import br.com.fitnesspro.shared.communication.constants.Timeouts
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
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
    @Transactional(timeout = Timeouts.OPERATION_LOW_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun login(@RequestBody @Valid validatedAuthenticationDTO: ValidatedAuthenticationDTO): ResponseEntity<ValidatedAuthenticationServiceResponse> {
        return ResponseEntity.ok(userService.login(validatedAuthenticationDTO))
    }

    @PostMapping(EndPointsV1.AUTHENTICATION_LOGOUT)
    @Transactional(timeout = Timeouts.OPERATION_LOW_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun logout(@RequestBody @Valid validatedAuthenticationDTO: ValidatedAuthenticationDTO): ResponseEntity<ValidatedAuthenticationServiceResponse> {
        return ResponseEntity.ok(userService.logout(validatedAuthenticationDTO))
    }
}