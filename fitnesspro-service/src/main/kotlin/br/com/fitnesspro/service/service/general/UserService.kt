package br.com.fitnesspro.service.service.general

import br.com.fitnesspro.core.helper.HashHelper
import br.com.fitnesspro.service.exception.UserNotFoundException
import br.com.fitnesspro.service.repository.general.user.IUserRepository
import br.com.fitnesspro.service.service.serviceauth.TokenService
import br.com.fitnesspro.shared.communication.dtos.general.AuthenticationDTO
import br.com.fitnesspro.shared.communication.dtos.serviceauth.ServiceTokenGenerationDTO
import br.com.fitnesspro.shared.communication.enums.serviceauth.EnumTokenType
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: IUserRepository,
    private val tokenService: TokenService
) {
    fun login(authenticationDTO: AuthenticationDTO): String {
        if (!HashHelper.isHashed(authenticationDTO.password!!)) {
            authenticationDTO.password = HashHelper.applyHash(authenticationDTO.password!!)
        }

        return userRepository.findByEmailAndPassword(authenticationDTO.email!!, authenticationDTO.password!!)?.let { user ->
            user.authenticated = true

            userRepository.save(user)

            val serviceTokenGenerationDTO = ServiceTokenGenerationDTO(type = EnumTokenType.USER_AUTHENTICATION_TOKEN, userId = user.id)
            tokenService.generateServiceToken(serviceTokenGenerationDTO).jwtToken
        } ?: throw UserNotFoundException("Usuário não encontrado")
    }

    fun logout(authenticationDTO: AuthenticationDTO) {
        userRepository.findByEmail(authenticationDTO.email!!)?.let { user ->
            user.authenticated = false

            userRepository.save(user)
        }
    }
}