package br.com.fitnesspro.authentication.service.mappers

import br.com.fitnesspro.authentication.repository.auditable.IPersonRepository
import br.com.fitnesspro.authentication.repository.jpa.IApplicationRepository
import br.com.fitnesspro.models.serviceauth.Device
import br.com.fitnesspro.service.communication.dtos.serviceauth.ValidatedDeviceDTO
import br.com.fitnesspro.shared.communication.dtos.serviceauth.interfaces.IDeviceDTO
import org.springframework.stereotype.Service

@Service
class DeviceServiceMapper(
    private val personServiceMapper: PersonServiceMapper,
    private val applicationServiceMapper: ApplicationServiceMapper,
    private val applicationRepository: IApplicationRepository,
    private val personRepository: IPersonRepository
) {

    fun getDeviceDTO(deviceModel: Device): ValidatedDeviceDTO {
        return ValidatedDeviceDTO(
            id = deviceModel.id,
            model = deviceModel.model,
            brand = deviceModel.brand,
            firebaseMessagingToken = deviceModel.firebaseMessagingToken,
            zoneId = deviceModel.zoneId,
            androidVersion = deviceModel.androidVersion,
            creationDate = deviceModel.creationDate,
            updateDate = deviceModel.updateDate,
            active = deviceModel.active,
            person = personServiceMapper.getPersonDTO(deviceModel.person!!),
            application = applicationServiceMapper.getApplicationDTO(deviceModel.application!!)
        )
    }

    fun getDevice(dto: IDeviceDTO): Device {
        return Device(
            id = dto.id!!,
            model = dto.model!!,
            brand = dto.brand!!,
            firebaseMessagingToken = dto.firebaseMessagingToken!!,
            zoneId = dto.zoneId!!,
            androidVersion = dto.androidVersion!!,
            active = dto.active,
            person = personRepository.findById(dto.person?.id!!).get(),
            application = applicationRepository.findById(dto.application?.id!!).get()
        )
    }
}