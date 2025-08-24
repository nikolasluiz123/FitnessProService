package br.com.fitnesspro.authentication.service

import br.com.fitnesspro.authentication.repository.auditable.IDeviceRepository
import br.com.fitnesspro.authentication.repository.jpa.ICustomDeviceRepository
import br.com.fitnesspro.authentication.service.mappers.DeviceServiceMapper
import br.com.fitnesspro.service.communication.dtos.serviceauth.ValidatedDeviceDTO
import br.com.fitnesspro.shared.communication.dtos.serviceauth.interfaces.IDeviceDTO
import br.com.fitnesspro.shared.communication.paging.PageInfos
import br.com.fitnesspro.shared.communication.query.filter.DeviceFilter
import org.springframework.stereotype.Service

@Service
class DeviceService(
    private val deviceRepository: IDeviceRepository,
    private val customDeviceRepository: ICustomDeviceRepository,
    private val tokenService: TokenService,
    private val deviceServiceMapper: DeviceServiceMapper
) {

    fun saveDevice(deviceDTO: IDeviceDTO, applicationJWT: String) {
        val applicationDTO = tokenService.getServiceTokenDTO(applicationJWT)?.application
        deviceDTO.application = applicationDTO

        val otherPersonDevices = customDeviceRepository.getListDeviceFrom(deviceDTO.person?.id!!).filter {
            it.id != deviceDTO.id && it.active
        }

        if (otherPersonDevices.isNotEmpty()) {
            otherPersonDevices.forEach {
                it.active = false
            }

            deviceRepository.saveAll(otherPersonDevices)
        }

        val device = deviceServiceMapper.getDevice(deviceDTO)
        deviceRepository.save(device)
    }

    fun getListDevice(filter: DeviceFilter, pageInfos: PageInfos): List<ValidatedDeviceDTO> {
        return customDeviceRepository.getListDevice(filter, pageInfos).map(deviceServiceMapper::getDeviceDTO)
    }

    fun getCountListDevice(filter: DeviceFilter): Int {
        return customDeviceRepository.getCountListDevice(filter)
    }

    fun getDevicesWithFirebaseMessagingTokens(tokens: List<String>): List<ValidatedDeviceDTO> {
        return deviceRepository.findByFirebaseMessagingTokenIn(tokens).map(deviceServiceMapper::getDeviceDTO)
    }

    fun getFirebaseMessagingTokenFromDevicesWithIds(ids: List<String>): List<String> {
        return deviceRepository.findByIdIn(ids).mapNotNull {
            deviceServiceMapper.getDeviceDTO(it).firebaseMessagingToken
        }
    }

    fun getDeviceDTOWithIds(ids: List<String>): List<ValidatedDeviceDTO> {
        return deviceRepository.findByIdIn(ids).map(deviceServiceMapper::getDeviceDTO)
    }

    fun getFirebaseMessagingTokenFromDevicesWithPersonIds(personIds: List<String>): List<String> {
        return deviceRepository.findByPersonIdInAndActiveIsTrue(personIds).mapNotNull {
            deviceServiceMapper.getDeviceDTO(it).firebaseMessagingToken
        }
    }

    fun getFirebaseMessagingTokenFromAllDevices(): List<String> {
        return deviceRepository.findByActiveIsTrueAndFirebaseMessagingTokenIsNotNull().map {
            deviceServiceMapper.getDeviceDTO(it).firebaseMessagingToken!!
        }
    }

    fun getDeviceFromPerson(personId: String): ValidatedDeviceDTO? {
        return deviceRepository.findByPersonIdAndActiveIsTrue(personId)?.let(deviceServiceMapper::getDeviceDTO)
    }

    fun inactivatePersonDevice(personId: String) {
        deviceRepository.findByPersonIdAndActiveIsTrue(personId)?.apply {
            active = false
            deviceRepository.save(this)
        }
    }
}