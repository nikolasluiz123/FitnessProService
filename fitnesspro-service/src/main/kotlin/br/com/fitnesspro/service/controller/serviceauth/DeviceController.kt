package br.com.fitnesspro.service.controller.serviceauth

import br.com.fitnesspro.service.config.gson.defaultGSon
import br.com.fitnesspro.service.service.serviceauth.DeviceService
import br.com.fitnesspro.shared.communication.constants.EndPointsV1
import br.com.fitnesspro.shared.communication.constants.Timeouts
import br.com.fitnesspro.shared.communication.dtos.serviceauth.DeviceDTO
import br.com.fitnesspro.shared.communication.paging.CommonPageInfos
import br.com.fitnesspro.shared.communication.query.filter.DeviceFilter
import br.com.fitnesspro.shared.communication.responses.ReadServiceResponse
import br.com.fitnesspro.shared.communication.responses.SingleValueServiceResponse
import com.google.gson.GsonBuilder
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(EndPointsV1.DEVICE_V1)
@Tag(name = "Device Controller", description = "Manutenção dos Dispositivos Móveis")
class DeviceController(
    private val deviceService: DeviceService
) {

    @GetMapping
    @Transactional(timeout = Timeouts.OPERATION_MEDIUM_TIMEOUT)
    @SecurityRequirement(name = "Bearer Authentication")
    fun getListDevice(@RequestParam filter: String, @RequestParam pageInfos: String): ResponseEntity<ReadServiceResponse<DeviceDTO>> {
        val defaultGSon = GsonBuilder().defaultGSon()
        val queryFilter = defaultGSon.fromJson(filter, DeviceFilter::class.java)
        val commonPageInfos = defaultGSon.fromJson(pageInfos, CommonPageInfos::class.java)

        val logs = deviceService.getListDevice(queryFilter, commonPageInfos)
        return ResponseEntity.ok(ReadServiceResponse(values = logs, code = HttpStatus.OK.value(), success = true))
    }

    @GetMapping(EndPointsV1.DEVICE_COUNT)
    @Transactional(timeout = Timeouts.OPERATION_MEDIUM_TIMEOUT)
    @SecurityRequirement(name = "Bearer Authentication")
    fun getCountListDevice(@RequestParam filter: String): ResponseEntity<SingleValueServiceResponse<Int>> {
        val defaultGSon = GsonBuilder().defaultGSon()
        val queryFilter = defaultGSon.fromJson(filter, DeviceFilter::class.java)

        val count = deviceService.getCountListDevice(queryFilter)
        return ResponseEntity.ok(SingleValueServiceResponse(value = count, code = HttpStatus.OK.value(), success = true))
    }

}