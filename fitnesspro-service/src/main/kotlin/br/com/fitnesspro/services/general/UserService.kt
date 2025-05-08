package br.com.fitnesspro.services.general

import br.com.fitnesspro.exception.BusinessException
import br.com.fitnesspro.exception.UserNotFoundException
import br.com.fitnesspro.models.general.User
import br.com.fitnesspro.repository.general.user.IUserRepository
import br.com.fitnesspro.services.serviceauth.DeviceService
import br.com.fitnesspro.services.serviceauth.TokenService
import br.com.fitnesspro.shared.communication.dtos.general.AuthenticationDTO
import br.com.fitnesspro.shared.communication.dtos.serviceauth.ServiceTokenDTO
import br.com.fitnesspro.shared.communication.dtos.serviceauth.ServiceTokenGenerationDTO
import br.com.fitnesspro.shared.communication.enums.general.EnumUserType
import br.com.fitnesspro.shared.communication.enums.serviceauth.EnumTokenType
import br.com.fitnesspro.shared.communication.helper.HashHelper
import br.com.fitnesspro.shared.communication.responses.AuthenticationServiceResponse
import org.springframework.context.MessageSource
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(
    private val userRepository: IUserRepository,
    private val tokenService: TokenService,
    private val deviceService: DeviceService,
    private val messageSource: MessageSource
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
            val message = messageSource.getMessage("auth.error.invalid.credentials", null, Locale.getDefault())

            AuthenticationServiceResponse(
                code = HttpStatus.NOT_FOUND.value(),
                success = false,
                error = message
            )
        }
    }

    private fun getValidatedUser(authenticationDTO: AuthenticationDTO): User {
        val user = userRepository.findByEmailAndPassword(authenticationDTO.email!!, authenticationDTO.password!!)

        if (user == null) {
            val message = messageSource.getMessage("auth.error.user.not.found", null, Locale.getDefault())
            throw UserNotFoundException(message)
        }

        if (authenticationDTO.adminAuth && user.type != EnumUserType.ADMINISTRATOR) {
            val message = messageSource.getMessage("auth.error.user.permission.denied", null, Locale.getDefault())
            throw BusinessException(message)
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