package br.com.fitnesspro.authentication.service

import br.com.fitnesspro.authentication.repository.auditable.IDeviceRepository
import br.com.fitnesspro.authentication.repository.auditable.IUserRepository
import br.com.fitnesspro.authentication.repository.jpa.IApplicationRepository
import br.com.fitnesspro.authentication.service.mappers.TokenServiceMapper
import br.com.fitnesspro.authentication.repository.jpa.ICustomServiceTokenRepository
import br.com.fitnesspro.authentication.repository.jpa.IServiceTokenRepository
import br.com.fitnesspro.core.exceptions.BusinessException
import br.com.fitnesspro.core.extensions.dateTimeNow
import br.com.fitnesspro.models.serviceauth.ServiceToken
import br.com.fitnesspro.shared.communication.dtos.general.UserDTO
import br.com.fitnesspro.shared.communication.dtos.serviceauth.ApplicationDTO
import br.com.fitnesspro.shared.communication.dtos.serviceauth.DeviceDTO
import br.com.fitnesspro.shared.communication.dtos.serviceauth.ServiceTokenDTO
import br.com.fitnesspro.shared.communication.dtos.serviceauth.ServiceTokenGenerationDTO
import br.com.fitnesspro.shared.communication.enums.serviceauth.EnumTokenType
import br.com.fitnesspro.shared.communication.exception.ExpiredTokenException
import br.com.fitnesspro.shared.communication.exception.NotFoundTokenException
import br.com.fitnesspro.shared.communication.paging.CommonPageInfos
import br.com.fitnesspro.shared.communication.query.filter.ServiceTokenFilter
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.HttpServletRequest
import org.springframework.context.MessageSource
import org.springframework.security.crypto.keygen.Base64StringKeyGenerator
import org.springframework.stereotype.Service
import java.util.*
import javax.crypto.SecretKey
import kotlin.jvm.optionals.getOrElse
import kotlin.jvm.optionals.getOrNull

@Service
class TokenService(
    private val tokenRepository: IServiceTokenRepository,
    private val customServiceTokenRepository: ICustomServiceTokenRepository,
    private val deviceRepository: IDeviceRepository,
    private val userRepository: IUserRepository,
    private val applicationRepository: IApplicationRepository,
    private val tokenServiceMapper: TokenServiceMapper,
    private val messageSource: MessageSource
) {

    private final val secretKey = System.getenv("JWT_SECRET")

    fun generateSecretKey(): String {
        val secretKeyGenerator = Base64StringKeyGenerator(64)
        return secretKeyGenerator.generateKey()
    }

    fun generateTokenJWT(username: String): String {
        return Jwts
            .builder()
            .subject(username)
            .signWith(getSignInKey(), Jwts.SIG.HS256)
            .compact()
    }

    private fun getSignInKey(): SecretKey {
        val keyBytes: ByteArray = Decoders.BASE64.decode(secretKey)
        return Keys.hmacShaKeyFor(keyBytes)
    }

    fun generateServiceToken(dto: ServiceTokenGenerationDTO): ServiceTokenDTO {
        when (dto.type!!) {
            EnumTokenType.USER_AUTHENTICATION_TOKEN -> {
                validateEntityId(dto.userId, dto::userId.name, dto.type)

                val creationDate = dateTimeNow()
                val expirationDate = creationDate.plusHours(1)
                val user = userRepository.findById(dto.userId!!).get()

                val serviceToken = tokenServiceMapper.getServiceTokenDTO(
                    jwt = generateTokenJWT(user.username),
                    creationDate = creationDate,
                    expirationDate = expirationDate,
                    type = dto.type!!,
                    user = user
                )

                saveServiceToken(serviceToken)

                return serviceToken
            }

            EnumTokenType.DEVICE_TOKEN -> {
                validateEntityId(dto.deviceId, dto::deviceId.name, dto.type)

                val creationDate = dateTimeNow()
                val expirationDate = creationDate.plusHours(12)
                val device = deviceRepository.findById(dto.deviceId!!).get()

                val serviceToken = tokenServiceMapper.getServiceTokenDTO(
                    jwt = generateTokenJWT(device.id),
                    creationDate = creationDate,
                    expirationDate = expirationDate,
                    type = dto.type!!,
                    device = device
                )

                saveServiceToken(serviceToken)

                return serviceToken
            }

            EnumTokenType.APPLICATION_TOKEN -> {
                validateEntityId(dto.applicationId, dto::applicationId.name, dto.type)

                val creationDate = dateTimeNow()
                val application = applicationRepository.findById(dto.applicationId!!).get()

                val serviceToken = tokenServiceMapper.getServiceTokenDTO(
                    jwt = generateTokenJWT(application.id),
                    creationDate = creationDate,
                    expirationDate = null,
                    type = dto.type!!,
                    application = application
                )

                saveServiceToken(serviceToken)

                return serviceToken
            }
        }
    }

    private fun validateEntityId(id: String?, fieldName: String, tokenType: EnumTokenType?) {
        if (id.isNullOrEmpty()) {
            val message = messageSource.getMessage(
                "service.token.error.required.field.token.generation",
                arrayOf(tokenType?.name!!, fieldName),
                Locale.getDefault()
            )

            throw BusinessException(message)
        }
    }

    fun invalidateToken(tokenId: String) {
        val serviceToken = tokenRepository.findById(tokenId).getOrElse {
            throw NotFoundTokenException(
                messageSource.getMessage("core.service.error.token.not.found", null, Locale.getDefault())
            )
        }
        serviceToken.expirationDate = dateTimeNow()

        tokenRepository.save(serviceToken)
    }

    fun invalidateAllUserTokens(userId: String): List<ServiceTokenDTO> {
        val tokens = customServiceTokenRepository.getListServiceTokenNotExpired(userId = userId)
        invalidateTokensList(tokens)

        return tokens.map { it.toServiceTokenDTO() }
    }

    fun invalidateAllTokens() {
        val tokens = tokenRepository.findAll()
        invalidateTokensList(tokens)
    }

    private fun invalidateTokensList(tokens: List<ServiceToken>) {
        tokens.forEach {
            it.expirationDate = dateTimeNow()
        }

        tokenRepository.saveAll(tokens)
    }

    private fun saveServiceToken(serviceTokenDTO: ServiceTokenDTO) {
        val token = tokenServiceMapper.getTokenService(serviceTokenDTO)
        tokenRepository.save(token)
        serviceTokenDTO.id = token.id
    }

    fun getValidatedServiceToken(jwtToken: String): ServiceTokenDTO {
        val dto = getServiceTokenDTO(jwtToken) ?: throw NotFoundTokenException(
            messageSource.getMessage("core.service.error.token.not.found", null, Locale.getDefault())
        )

        if (isTokenExpired(dto)) {
            throw ExpiredTokenException(
                messageSource.getMessage("core.service.error.token.expired", null, Locale.getDefault())
            )
        }

        return dto
    }

    fun isTokenExpired(dto: ServiceTokenDTO): Boolean {
        return dto.expirationDate != null && dateTimeNow().isAfter(dto.expirationDate)
    }

    fun getServiceTokenDTO(jwtToken: String): ServiceTokenDTO? {
        return customServiceTokenRepository.findValidServiceToken(jwtToken)?.toServiceTokenDTO()
    }

    fun findServiceTokenDTOById(tokenId: String): ServiceTokenDTO? {
        return tokenRepository.findById(tokenId).getOrNull()?.toServiceTokenDTO()
    }

    fun getListServiceToken(filter: ServiceTokenFilter, pageInfos: CommonPageInfos): List<ServiceTokenDTO> {
        return customServiceTokenRepository.getListServiceToken(filter, pageInfos).map { it.toServiceTokenDTO() }
    }

    fun getCountListServiceToken(filter: ServiceTokenFilter): Int {
        return customServiceTokenRepository.getCountListServiceToken(filter)
    }

    fun getServiceTokenFromRequest(request: HttpServletRequest): ServiceTokenDTO? {
        val jwtToken = getJWTTokenFromRequest(request) ?: return null
        return getServiceTokenDTO(jwtToken)
    }

    fun getRequestAuthorization(request: HttpServletRequest): String? {
        return request.getHeader("Authorization")
    }

    fun hasBearerToken(request: HttpServletRequest): Boolean {
        return getRequestAuthorization(request)?.startsWith("Bearer ") ?: false
    }

    fun getJWTTokenFromRequest(request: HttpServletRequest): String? {
        return getRequestAuthorization(request)?.substring(7)
    }

    private fun ServiceToken.toServiceTokenDTO(): ServiceTokenDTO {
        return ServiceTokenDTO(
            id = id,
            jwtToken = jwtToken,
            creationDate = creationDate,
            expirationDate = expirationDate,
            type = type,
            user = getUserDTOFromServiceToken(this),
            device = getDeviceDTOFromServiceToken(this),
            application = getApplicationDTOFromServiceToken(this)
        )
    }

    private fun getDeviceDTOFromServiceToken(serviceToken: ServiceToken): DeviceDTO? {
        return serviceToken.device?.run {
            DeviceDTO(
                id = id,
                model = model,
                brand = brand,
                androidVersion = androidVersion,
                creationDate = creationDate,
                updateDate = updateDate,
                active = active,
                application = ApplicationDTO(
                    id = application?.id,
                    name = application?.name,
                    active = application?.active ?: false
                )
            )
        }
    }

    private fun getUserDTOFromServiceToken(serviceToken: ServiceToken): UserDTO? {
        return serviceToken.user?.run {
            UserDTO(
                id = id,
                creationDate = creationDate,
                updateDate = updateDate,
                active = active,
                email = email,
                password = password,
                type = type,
            )
        }
    }

    private fun getApplicationDTOFromServiceToken(serviceToken: ServiceToken): ApplicationDTO? {
        return serviceToken.application?.run {
            ApplicationDTO(
                id = id,
                name = name
            )
        }
    }
}