package br.com.fitnesspro.service.service.serviceauth

import br.com.fitnesspro.service.models.serviceauth.Application
import br.com.fitnesspro.service.models.serviceauth.Device
import br.com.fitnesspro.service.repository.serviceauth.ICustomDeviceRepository
import br.com.fitnesspro.service.repository.serviceauth.IDeviceRepository
import br.com.fitnesspro.shared.communication.dtos.serviceauth.DeviceDTO
import br.com.fitnesspro.shared.communication.paging.PageInfos
import br.com.fitnesspro.shared.communication.query.filter.DeviceFilter
import org.springframework.stereotype.Service

@Service
class DeviceService(
    private val deviceRepository: IDeviceRepository,
    private val customDeviceRepository: ICustomDeviceRepository,
    private val tokenService: TokenService
) {

    fun saveDevice(deviceDTO: DeviceDTO, applicationJWT: String) {
        val applicationDTO = tokenService.getServiceTokenDTO(applicationJWT)?.application

        val device = Device(
            id = deviceDTO.id!!,
            model = deviceDTO.model!!,
            brand = deviceDTO.brand!!,
            androidVersion = deviceDTO.androidVersion!!,
            application = Application(
                id = applicationDTO?.id!!,
                name = applicationDTO.name,
                active = applicationDTO.active
            )
        )

        deviceRepository.save(device)
    }

    fun getListDevice(filter: DeviceFilter, pageInfos: PageInfos): List<DeviceDTO> {
        return customDeviceRepository.getListDevice(filter, pageInfos).map { it.toDeviceDTO() }
    }

    fun getCountListDevice(filter: DeviceFilter): Int {
        return customDeviceRepository.getCountListDevice(filter)
    }

    private fun Device.toDeviceDTO(): DeviceDTO {
        return DeviceDTO(
            id = id,
            model = model,
            creationDate = creationDate,
            updateDate = updateDate,
            active = active
        )
    }
}