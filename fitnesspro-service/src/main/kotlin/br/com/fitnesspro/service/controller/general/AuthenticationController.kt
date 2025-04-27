package br.com.fitnesspro.service.controller.general

import br.com.fitnesspro.service.service.general.UserService
import br.com.fitnesspro.shared.communication.constants.EndPointsV1
import br.com.fitnesspro.shared.communication.constants.Timeouts
import br.com.fitnesspro.shared.communication.dtos.general.AuthenticationDTO
import br.com.fitnesspro.shared.communication.responses.AuthenticationServiceResponse
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
    fun login(@RequestBody @Valid authenticationDTO: AuthenticationDTO): ResponseEntity<AuthenticationServiceResponse> {
        return ResponseEntity.ok(userService.login(authenticationDTO))
    }

    @PostMapping(EndPointsV1.AUTHENTICATION_LOGOUT)
    @Transactional(timeout = Timeouts.OPERATION_LOW_TIMEOUT, rollbackFor = [Exception::class])
    @SecurityRequirement(name = "Bearer Authentication")
    fun logout(@RequestBody @Valid authenticationDTO: AuthenticationDTO): ResponseEntity<AuthenticationServiceResponse> {
        return ResponseEntity.ok(userService.logout(authenticationDTO))
    }
}