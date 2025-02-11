package br.com.fitnesspro.service.general

import br.com.fitnesspro.config.application.JWTService
import br.com.fitnesspro.dto.general.AuthenticationDTO
import br.com.fitnesspro.helper.HashHelper
import br.com.fitnesspro.repository.general.user.IUserRepository
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: IUserRepository,
    private val jwtService: JWTService
) {
    fun login(authenticationDTO: AuthenticationDTO): String {
        if (!HashHelper.isHashed(authenticationDTO.password!!)) {
            authenticationDTO.password = HashHelper.applyHash(authenticationDTO.password!!)
        }

        return userRepository.findByEmailAndPassword(authenticationDTO.email!!, authenticationDTO.password!!)?.let { user ->
            user.authenticated = true

            userRepository.save(user)

            jwtService.generateToken(user)
        } ?: throw UsernameNotFoundException("Usuário não encontrado")
    }

    fun logout(authenticationDTO: AuthenticationDTO) {
        userRepository.findByEmail(authenticationDTO.email!!)?.let { user ->
            user.authenticated = false

            userRepository.save(user)
        }
    }
}