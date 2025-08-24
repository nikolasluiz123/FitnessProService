package br.com.fitnesspro.shared.communication.dtos.serviceauth

import br.com.fitnesspro.shared.communication.dtos.general.interfaces.IPersonDTO
import br.com.fitnesspro.shared.communication.dtos.serviceauth.interfaces.IApplicationDTO
import br.com.fitnesspro.shared.communication.dtos.serviceauth.interfaces.IDeviceDTO
import java.time.LocalDateTime

data class DeviceDTO(
    override var id: String? = null,
    override val model: String? = null,
    override val brand: String? = null,
    override val androidVersion: String? = null,
    override val firebaseMessagingToken: String? = null,
    override var zoneId: String? = null,
    override var creationDate: LocalDateTime? = null,
    override var updateDate: LocalDateTime? = null,
    override var active: Boolean = true,
    override var application: IApplicationDTO? = null,
    override var person: IPersonDTO? = null
): IDeviceDTO
