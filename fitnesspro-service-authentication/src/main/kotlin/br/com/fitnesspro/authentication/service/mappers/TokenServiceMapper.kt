package br.com.fitnesspro.authentication.service.mappers

import br.com.fitnesspro.authentication.repository.auditable.IDeviceRepository
import br.com.fitnesspro.authentication.repository.auditable.IUserRepository
import br.com.fitnesspro.authentication.repository.jpa.IApplicationRepository
import br.com.fitnesspro.models.general.User
import br.com.fitnesspro.models.serviceauth.Application
import br.com.fitnesspro.models.serviceauth.Device
import br.com.fitnesspro.models.serviceauth.ServiceToken
import br.com.fitnesspro.service.communication.dtos.serviceauth.ValidatedServiceTokenDTO
import br.com.fitnesspro.shared.communication.enums.serviceauth.EnumTokenType
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class TokenServiceMapper(
    private val userRepository: IUserRepository,
    private val deviceRepository: IDeviceRepository,
    private val applicationRepository: IApplicationRepository,
    private val personServiceMapper: PersonServiceMapper,
    private val userServiceMapper: UserServiceMapper,
    private val deviceServiceMapper: DeviceServiceMapper,
    private val applicationServiceMapper: ApplicationServiceMapper
) {

    fun getTokenService(dto: ValidatedServiceTokenDTO): ServiceToken {
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
    ) : ValidatedServiceTokenDTO {
        return ValidatedServiceTokenDTO(
            jwtToken = jwt,
            creationDate = creationDate,
            expirationDate = expirationDate,
            type = type,
            user = user?.let { userServiceMapper.getUserDTO(it) },
            device = device?.let { deviceServiceMapper.getDeviceDTO(it) },
            application = application?.let { applicationServiceMapper.getApplicationDTO(it) }
        )
    }
}