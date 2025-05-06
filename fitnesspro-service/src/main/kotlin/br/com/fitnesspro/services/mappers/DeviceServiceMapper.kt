package br.com.fitnesspro.services.mappers

import br.com.fitnesspro.models.serviceauth.Device
import br.com.fitnesspro.repository.general.person.IPersonRepository
import br.com.fitnesspro.repository.serviceauth.IApplicationRepository
import br.com.fitnesspro.shared.communication.dtos.serviceauth.DeviceDTO
import org.springframework.stereotype.Service

@Service
class DeviceServiceMapper(
    private val personServiceMapper: PersonServiceMapper,
    private val applicationServiceMapper: ApplicationServiceMapper,
    private val applicationRepository: IApplicationRepository,
    private val personRepository: IPersonRepository
) {

    fun getDeviceDTO(deviceModel: Device): DeviceDTO {
        return DeviceDTO(
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

    fun getDevice(dto: DeviceDTO): Device {
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