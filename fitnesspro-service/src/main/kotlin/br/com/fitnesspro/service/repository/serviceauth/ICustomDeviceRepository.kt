package br.com.fitnesspro.service.repository.serviceauth

import br.com.fitnesspro.service.models.serviceauth.Device
import br.com.fitnesspro.shared.communication.paging.PageInfos
import br.com.fitnesspro.shared.communication.query.filter.DeviceFilter

interface ICustomDeviceRepository {

    fun getListDevice(filter: DeviceFilter, pageInfos: PageInfos): List<Device>

    fun getCountListDevice(filter: DeviceFilter): Int
}