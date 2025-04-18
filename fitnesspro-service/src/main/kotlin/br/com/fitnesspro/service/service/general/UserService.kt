package br.com.fitnesspro.service.service.general

import br.com.fitnesspro.core.helper.HashHelper
import br.com.fitnesspro.service.exception.BusinessException
import br.com.fitnesspro.service.repository.general.user.IUserRepository
import br.com.fitnesspro.service.service.serviceauth.TokenService
import br.com.fitnesspro.shared.communication.dtos.general.AuthenticationDTO
import br.com.fitnesspro.shared.communication.dtos.serviceauth.ServiceTokenGenerationDTO
import br.com.fitnesspro.shared.communication.enums.general.EnumUserType
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

        val user = userRepository.findByEmailAndPassword(authenticationDTO.email!!, authenticationDTO.password!!)

        if (user == null) {
            throw BusinessException("Usuário não encontrado")
        }

        if (authenticationDTO.adminAuth && user.type != EnumUserType.ADMINISTRATOR) {
            throw BusinessException("Usuário não autorizado")
        }

        val serviceTokenGenerationDTO = ServiceTokenGenerationDTO(type = EnumTokenType.USER_AUTHENTICATION_TOKEN, userId = user.id)

        return tokenService.generateServiceToken(serviceTokenGenerationDTO).jwtToken!!
    }

    fun logout(authenticationDTO: AuthenticationDTO) {
        userRepository.findByEmail(authenticationDTO.email!!)?.let { user ->
            tokenService.invalidateAllUserTokens(user.id!!)
        }
    }
}