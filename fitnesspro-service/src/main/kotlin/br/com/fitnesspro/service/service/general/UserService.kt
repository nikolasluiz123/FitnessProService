package br.com.fitnesspro.service.service.general

import br.com.fitnesspro.core.helper.HashHelper
import br.com.fitnesspro.service.exception.BusinessException
import br.com.fitnesspro.service.exception.UserNotFoundException
import br.com.fitnesspro.service.models.general.User
import br.com.fitnesspro.service.repository.general.user.IUserRepository
import br.com.fitnesspro.service.service.serviceauth.DeviceService
import br.com.fitnesspro.service.service.serviceauth.TokenService
import br.com.fitnesspro.shared.communication.dtos.general.AuthenticationDTO
import br.com.fitnesspro.shared.communication.dtos.serviceauth.ServiceTokenDTO
import br.com.fitnesspro.shared.communication.dtos.serviceauth.ServiceTokenGenerationDTO
import br.com.fitnesspro.shared.communication.enums.general.EnumUserType
import br.com.fitnesspro.shared.communication.enums.serviceauth.EnumTokenType
import br.com.fitnesspro.shared.communication.responses.AuthenticationServiceResponse
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: IUserRepository,
    private val tokenService: TokenService,
    private val deviceService: DeviceService
) {
    fun login(authenticationDTO: AuthenticationDTO): AuthenticationServiceResponse {
        return try {
            hashPasswordIfNeed(authenticationDTO)
            val user = getValidatedUser(authenticationDTO)

            val userToken = getUserToken(user)
            val deviceToken = authenticationDTO.deviceDTO?.let { deviceDTO ->
                deviceService.saveDevice(
                    deviceDTO = deviceDTO,
                    applicationJWT = authenticationDTO.applicationJWT!!
                )

                getDeviceToken(deviceDTO.id!!)
            }

            AuthenticationServiceResponse(
                tokens = listOfNotNull(userToken, deviceToken),
                code = HttpStatus.OK.value(),
                success = true
            )
        } catch (_: UserNotFoundException) {
            AuthenticationServiceResponse(
                code = HttpStatus.NOT_FOUND.value(),
                success = false,
                error = "Credenciais inválidas, por favor, redigite."
            )
        }
    }

    private fun getValidatedUser(authenticationDTO: AuthenticationDTO): User {
        val user = userRepository.findByEmailAndPassword(authenticationDTO.email!!, authenticationDTO.password!!)

        if (user == null) {
            throw BusinessException("Usuário não encontrado")
        }

        if (authenticationDTO.adminAuth && user.type != EnumUserType.ADMINISTRATOR) {
            throw BusinessException("Usuário não autorizado")
        }
        return user
    }

    private fun hashPasswordIfNeed(authenticationDTO: AuthenticationDTO) {
        if (!HashHelper.isHashed(authenticationDTO.password!!)) {
            authenticationDTO.password = HashHelper.applyHash(authenticationDTO.password!!)
        }
    }

    private fun getUserToken(user: User): ServiceTokenDTO {
        val serviceTokenGenerationDTO = ServiceTokenGenerationDTO(
            type = EnumTokenType.USER_AUTHENTICATION_TOKEN,
            userId = user.id
        )

        return tokenService.generateServiceToken(serviceTokenGenerationDTO)
    }

    private fun getDeviceToken(deviceId: String): ServiceTokenDTO {
        val serviceTokenGenerationDTO = ServiceTokenGenerationDTO(
            type = EnumTokenType.DEVICE_TOKEN,
            deviceId = deviceId
        )

        return tokenService.generateServiceToken(serviceTokenGenerationDTO)
    }

    fun logout(authenticationDTO: AuthenticationDTO): AuthenticationServiceResponse {
        val userId = userRepository.findByEmail(authenticationDTO.email!!)?.id!!
        val userTokens = tokenService.invalidateAllUserTokens(userId)

        return AuthenticationServiceResponse(
            tokens = userTokens,
            code = HttpStatus.OK.value(),
            success = true
        )
    }
}