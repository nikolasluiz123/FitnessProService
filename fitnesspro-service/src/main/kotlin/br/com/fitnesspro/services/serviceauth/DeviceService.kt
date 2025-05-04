package br.com.fitnesspro.services.serviceauth

import br.com.fitnesspro.models.serviceauth.Application
import br.com.fitnesspro.models.serviceauth.Device
import br.com.fitnesspro.repository.general.person.IPersonRepository
import br.com.fitnesspro.repository.serviceauth.ICustomDeviceRepository
import br.com.fitnesspro.repository.serviceauth.IDeviceRepository
import br.com.fitnesspro.shared.communication.dtos.general.PersonDTO
import br.com.fitnesspro.shared.communication.dtos.general.UserDTO
import br.com.fitnesspro.shared.communication.dtos.serviceauth.DeviceDTO
import br.com.fitnesspro.shared.communication.paging.PageInfos
import br.com.fitnesspro.shared.communication.query.filter.DeviceFilter
import org.springframework.stereotype.Service

@Service
class DeviceService(
    private val deviceRepository: IDeviceRepository,
    private val customDeviceRepository: ICustomDeviceRepository,
    private val tokenService: TokenService,
    private val personRepository: IPersonRepository
) {

    fun saveDevice(deviceDTO: DeviceDTO, applicationJWT: String) {
        val applicationDTO = tokenService.getServiceTokenDTO(applicationJWT)?.application

        val otherPersonDevices = customDeviceRepository.getListDeviceFrom(deviceDTO.person?.id!!).filter {
            it.id != deviceDTO.id && it.active
        }

        if (otherPersonDevices.isNotEmpty()) {
            otherPersonDevices.forEach {
                it.active = false
            }

            deviceRepository.saveAll(otherPersonDevices)
        }

        val device = Device(
            id = deviceDTO.id!!,
            model = deviceDTO.model!!,
            brand = deviceDTO.brand!!,
            androidVersion = deviceDTO.androidVersion!!,
            firebaseMessagingToken = deviceDTO.firebaseMessagingToken!!,
            zoneId = deviceDTO.zoneId!!,
            application = Application(
                id = applicationDTO?.id!!,
                name = applicationDTO.name,
                active = applicationDTO.active
            ),
            person = personRepository.findById(deviceDTO.person?.id!!).get()
        )

        deviceRepository.save(device)
    }

    fun getListDevice(filter: DeviceFilter, pageInfos: PageInfos): List<DeviceDTO> {
        return customDeviceRepository.getListDevice(filter, pageInfos).map { it.toDeviceDTO() }
    }

    fun getCountListDevice(filter: DeviceFilter): Int {
        return customDeviceRepository.getCountListDevice(filter)
    }

    fun getDevicesWithFirebaseMessagingTokens(tokens: List<String>): List<DeviceDTO> {
        return deviceRepository.findByFirebaseMessagingTokenIn(tokens).map { it.toDeviceDTO() }
    }

    fun getFirebaseMessagingTokenFromDevicesWithIds(ids: List<String>): List<String> {
        return deviceRepository.findByIdIn(ids).mapNotNull { it.toDeviceDTO().firebaseMessagingToken }
    }

    fun getDeviceDTOWithIds(ids: List<String>): List<DeviceDTO> {
        return deviceRepository.findByIdIn(ids).map { it.toDeviceDTO() }
    }

    fun getFirebaseMessagingTokenFromDevicesWithPersonIds(personIds: List<String>): List<String> {
        return deviceRepository.findByPersonIdInAndActiveIsTrue(personIds).mapNotNull { it.toDeviceDTO().firebaseMessagingToken }
    }

    fun getFirebaseMessagingTokenFromAllDevices(): List<String> {
        return deviceRepository.findByActiveIsTrueAndFirebaseMessagingTokenIsNotNull().map { it.toDeviceDTO().firebaseMessagingToken!! }
    }

    fun getDeviceFromPerson(personId: String): DeviceDTO {
        return deviceRepository.findByPersonIdAndActiveIsTrue(personId).toDeviceDTO()
    }

    private fun Device.toDeviceDTO(): DeviceDTO {
        return DeviceDTO(
            id = id,
            model = model,
            brand = brand,
            firebaseMessagingToken = firebaseMessagingToken,
            zoneId = zoneId,
            person = PersonDTO(
                id = person?.id,
                name = person?.name,
                user = UserDTO(
                    id = person?.user?.id,
                    email = person?.user?.email,
                    password = person?.user?.password,
                    creationDate = person?.user?.creationDate,
                    updateDate = person?.user?.updateDate,
                    active = person?.user?.active!!
                ),
                creationDate = person?.creationDate,
                updateDate = person?.updateDate,
                active = person?.active!!,
                phone = person?.phone,
                birthDate = person?.birthDate
            ),
            androidVersion = androidVersion,
            creationDate = creationDate,
            updateDate = updateDate,
            active = active
        )
    }
}