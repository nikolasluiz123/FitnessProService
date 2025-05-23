package br.com.fitnesspro.services.mappers

import br.com.fitnesspro.models.general.User
import br.com.fitnesspro.models.serviceauth.Application
import br.com.fitnesspro.models.serviceauth.Device
import br.com.fitnesspro.models.serviceauth.ServiceToken
import br.com.fitnesspro.repository.general.user.IUserRepository
import br.com.fitnesspro.repository.serviceauth.IApplicationRepository
import br.com.fitnesspro.repository.serviceauth.IDeviceRepository
import br.com.fitnesspro.shared.communication.dtos.serviceauth.ServiceTokenDTO
import br.com.fitnesspro.shared.communication.enums.serviceauth.EnumTokenType
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class TokenServiceMapper(
    private val userRepository: IUserRepository,
    private val deviceRepository: IDeviceRepository,
    private val applicationRepository: IApplicationRepository,
    private val personServiceMapper: PersonServiceMapper,
    private val deviceServiceMapper: DeviceServiceMapper,
    private val applicationServiceMapper: ApplicationServiceMapper
) {

    fun getTokenService(dto: ServiceTokenDTO): ServiceToken {
        return ServiceToken(
            jwtToken = dto.jwtToken,
            creationDate = dto.creationDate,
            expirationDate = dto.expirationDate,
            type = dto.type,
            user = dto.user?.id?.let { userRepository.findById(it).get() },
            device = dto.device?.id?.let { deviceRepository.findById(it).get() },
            application = dto.application?.id?.let { applicationRepository.findById(it).get() }
        )
    }

    fun getServiceTokenDTO(
        jwt: String,
        type: EnumTokenType,
        creationDate: LocalDateTime,
        expirationDate: LocalDateTime?,
        user: User? = null,
        device: Device? = null,
        application: Application? = null
    ) : ServiceTokenDTO {
        return ServiceTokenDTO(
            jwtToken = jwt,
            creationDate = creationDate,
            expirationDate = expirationDate,
            type = type,
            user = user?.let { personServiceMapper.getUserDTO(it) },
            device = device?.let { deviceServiceMapper.getDeviceDTO(it) },
            application = application?.let { applicationServiceMapper.getApplicationDTO(it) }
        )
    }
}