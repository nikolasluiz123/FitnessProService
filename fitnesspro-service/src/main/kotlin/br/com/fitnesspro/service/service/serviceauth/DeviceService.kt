package br.com.fitnesspro.service.service.serviceauth

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
    private val customDeviceRepository: ICustomDeviceRepository
) {

    fun saveDevice(deviceDTO: DeviceDTO) {
        val device = deviceDTO.toDevice()

        deviceRepository.save(device)
        deviceDTO.id = device.id
    }

    fun getListDevice(filter: DeviceFilter, pageInfos: PageInfos): List<DeviceDTO> {
        return customDeviceRepository.getListDevice(filter, pageInfos).map { it.toDeviceDTO() }
    }

    fun getCountListDevice(filter: DeviceFilter): Int {
        return customDeviceRepository.getCountListDevice(filter)
    }

    private fun DeviceDTO.toDevice(): Device {
        val device = id?.let { deviceRepository.findById(it) }

        return when {
            id == null -> {
                Device(
                    model = model
                )
            }

            device?.isPresent ?: false -> {
                device!!.get().copy(model = model)
            }

            else -> {
                Device(
                    id = id!!,
                    model = model
                )
            }
        }
    }

    private fun Device.toDeviceDTO(): DeviceDTO {
        return DeviceDTO(
            id = id,
            model = model
        )
    }
}