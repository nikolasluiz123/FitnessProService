package br.com.fitnesspro.service.service.general

import br.com.fitnesspro.core.helper.HashHelper
import br.com.fitnesspro.service.config.application.JWTService
import br.com.fitnesspro.service.exception.UserNotFoundException
import br.com.fitnesspro.service.repository.general.user.IUserRepository
import br.com.fitnesspro.shared.communication.dtos.general.AuthenticationDTO
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: IUserRepository,
    private val jwtService: JWTService
) {
    fun login(authenticationDTO: AuthenticationDTO): String {
        throw Exception("Teste de exception")

        if (!HashHelper.isHashed(authenticationDTO.password!!)) {
            authenticationDTO.password = HashHelper.applyHash(authenticationDTO.password!!)
        }

        return userRepository.findByEmailAndPassword(authenticationDTO.email!!, authenticationDTO.password!!)?.let { user ->
            user.authenticated = true

            userRepository.save(user)

            jwtService.generateToken(user)
        } ?: throw UserNotFoundException("Usuário não encontrado")
    }

    fun logout(authenticationDTO: AuthenticationDTO) {
        userRepository.findByEmail(authenticationDTO.email!!)?.let { user ->
            user.authenticated = false

            userRepository.save(user)
        }
    }
}