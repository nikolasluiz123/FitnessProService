package br.com.fitnesspro.service.service.serviceauth

import br.com.fitnesspro.core.extensions.dateTimeNow
import br.com.fitnesspro.service.exception.BusinessException
import br.com.fitnesspro.service.models.serviceauth.ServiceToken
import br.com.fitnesspro.service.repository.general.user.IUserRepository
import br.com.fitnesspro.service.repository.serviceauth.IApplicationRepository
import br.com.fitnesspro.service.repository.serviceauth.ICustomServiceTokenRepository
import br.com.fitnesspro.service.repository.serviceauth.IDeviceRepository
import br.com.fitnesspro.service.repository.serviceauth.IServiceTokenRepository
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
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.security.crypto.keygen.Base64StringKeyGenerator
import org.springframework.stereotype.Service
import java.security.Key
import kotlin.jvm.optionals.getOrElse
import kotlin.jvm.optionals.getOrNull

@Service
class TokenService(
    private val tokenRepository: IServiceTokenRepository,
    private val customServiceTokenRepository: ICustomServiceTokenRepository,
    private val deviceRepository: IDeviceRepository,
    private val userRepository: IUserRepository,
    private val applicationRepository: IApplicationRepository,
) {

    private final val secretKey = System.getenv("JWT_SECRET")

    fun generateSecretKey(): String {
        val secretKeyGenerator = Base64StringKeyGenerator(64)
        return secretKeyGenerator.generateKey()
    }

    fun generateTokenJWT(username: String): String {
        return Jwts
            .builder()
            .setSubject(username)
            .signWith(getSignInKey(), SignatureAlgorithm.HS256)
            .compact()
    }

    private fun getSignInKey(): Key {
        val keyBytes: ByteArray = Decoders.BASE64.decode(secretKey)
        return Keys.hmacShaKeyFor(keyBytes)
    }

    fun generateServiceToken(dto: ServiceTokenGenerationDTO): ServiceTokenDTO {
        when (dto.type!!) {
            EnumTokenType.USER_AUTHENTICATION_TOKEN -> {
                if (dto.userId.isNullOrEmpty()) {
                    throw BusinessException("Para gerar um token USER_AUTHENTICATION_TOKEN é obrigatório informar userId")
                }

                val creationDate = dateTimeNow()
                val expirationDate = creationDate.plusHours(1)
                val user = userRepository.findById(dto.userId!!).get()

                val serviceToken = ServiceTokenDTO(
                    jwtToken = generateTokenJWT(user.username),
                    creationDate = creationDate,
                    expirationDate = expirationDate,
                    type = dto.type!!,
                    user = UserDTO(
                        id = user.id,
                        creationDate = user.creationDate,
                        updateDate = user.updateDate,
                        active = user.active,
                        email = user.email,
                        password = user.password,
                        type = user.type,
                    )
                )

                saveServiceToken(serviceToken)

                return serviceToken
            }

            EnumTokenType.DEVICE_TOKEN -> {
                if (dto.deviceId.isNullOrEmpty()) {
                    throw BusinessException("Para gerar um token DEVICE_TOKEN é obrigatório informar deviceId")
                }

                val creationDate = dateTimeNow()
                val expirationDate = creationDate.plusHours(12)
                val device = deviceRepository.findById(dto.deviceId!!).get()

                val serviceToken = ServiceTokenDTO(
                    jwtToken = generateTokenJWT(device.id),
                    creationDate = creationDate,
                    expirationDate = expirationDate,
                    type = dto.type!!,
                    device = DeviceDTO(
                        id = device.id,
                        model = device.model
                    )
                )

                saveServiceToken(serviceToken)

                return serviceToken
            }

            EnumTokenType.APPLICATION_TOKEN -> {
                val creationDate = dateTimeNow()
                val application = applicationRepository.findById(dto.applicationId!!).get()

                val serviceToken = ServiceTokenDTO(
                    jwtToken = generateTokenJWT(dto.applicationId!!),
                    creationDate = creationDate,
                    type = dto.type!!,
                    application = ApplicationDTO(
                        id = application.id,
                        name = application.name
                    )
                )

                saveServiceToken(serviceToken)

                return serviceToken
            }
        }
    }

    fun invalidateToken(tokenId: String) {
        val serviceToken = tokenRepository.findById(tokenId).getOrElse { throw NotFoundTokenException() }
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

    private fun saveServiceToken(serviceToken: ServiceTokenDTO) {
        val token = ServiceToken(
            jwtToken = serviceToken.jwtToken,
            creationDate = serviceToken.creationDate,
            expirationDate = serviceToken.expirationDate,
            type = serviceToken.type,
            user = serviceToken.user?.id?.let { userRepository.findById(it).get() },
            device = serviceToken.device?.id?.let { deviceRepository.findById(it).get() },
            application = serviceToken.application?.id?.let { applicationRepository.findById(it).get() }
        )

        tokenRepository.save(token)

        serviceToken.id = token.id
    }

    fun getValidatedServiceToken(jwtToken: String): ServiceTokenDTO {
        val dto = getServiceTokenDTO(jwtToken) ?: throw NotFoundTokenException()

        if (isTokenExpired(dto)) {
            throw ExpiredTokenException()
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