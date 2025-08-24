package br.com.fitnesspro.authentication.service

import br.com.fitnesspro.authentication.repository.auditable.IUserRepository
import br.com.fitnesspro.core.exceptions.BusinessException
import br.com.fitnesspro.core.exceptions.UserNotFoundException
import br.com.fitnesspro.models.general.User
import br.com.fitnesspro.service.communication.dtos.general.ValidatedAuthenticationDTO
import br.com.fitnesspro.service.communication.dtos.serviceauth.ValidatedServiceTokenDTO
import br.com.fitnesspro.service.communication.dtos.serviceauth.ValidatedServiceTokenGenerationDTO
import br.com.fitnesspro.service.communication.responses.ValidatedAuthenticationServiceResponse
import br.com.fitnesspro.shared.communication.enums.general.EnumUserType
import br.com.fitnesspro.shared.communication.enums.serviceauth.EnumTokenType
import br.com.fitnesspro.shared.communication.helper.HashHelper
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
    fun login(authenticationDTO: ValidatedAuthenticationDTO): ValidatedAuthenticationServiceResponse {
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

            ValidatedAuthenticationServiceResponse(
                tokens = listOfNotNull(userToken, deviceToken),
                code = HttpStatus.OK.value(),
                success = true
            )
        } catch (_: UserNotFoundException) {
            val message = messageSource.getMessage("auth.error.invalid.credentials", null, Locale.getDefault())

            ValidatedAuthenticationServiceResponse(
                code = HttpStatus.NOT_FOUND.value(),
                success = false,
                error = message
            )
        }
    }

    private fun getValidatedUser(authenticationDTO: ValidatedAuthenticationDTO): User {
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

    private fun hashPasswordIfNeed(authenticationDTO: ValidatedAuthenticationDTO) {
        if (!HashHelper.isHashed(authenticationDTO.password!!)) {
            authenticationDTO.password = HashHelper.applyHash(authenticationDTO.password!!)
        }
    }

    private fun getUserToken(user: User): ValidatedServiceTokenDTO {
        val serviceTokenGenerationDTO = ValidatedServiceTokenGenerationDTO(
            type = EnumTokenType.USER_AUTHENTICATION_TOKEN,
            userId = user.id
        )

        return tokenService.generateServiceToken(serviceTokenGenerationDTO)
    }

    private fun getDeviceToken(deviceId: String): ValidatedServiceTokenDTO {
        val serviceTokenGenerationDTO = ValidatedServiceTokenGenerationDTO(
            type = EnumTokenType.DEVICE_TOKEN,
            deviceId = deviceId
        )

        return tokenService.generateServiceToken(serviceTokenGenerationDTO)
    }

    fun logout(authenticationDTO: ValidatedAuthenticationDTO): ValidatedAuthenticationServiceResponse {
        val userId = userRepository.findByEmail(authenticationDTO.email!!)?.id!!
        val userTokens = tokenService.invalidateAllUserTokens(userId)

        return ValidatedAuthenticationServiceResponse(
            tokens = userTokens,
            code = HttpStatus.OK.value(),
            success = true
        )
    }
}