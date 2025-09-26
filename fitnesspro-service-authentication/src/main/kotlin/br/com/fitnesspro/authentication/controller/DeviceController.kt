package br.com.fitnesspro.authentication.controller

import br.com.fitnesspro.authentication.service.DeviceService
import br.com.fitnesspro.service.communication.dtos.serviceauth.ValidatedDeviceDTO
import br.com.fitnesspro.service.communication.gson.defaultServiceGSon
import br.com.fitnesspro.service.communication.responses.ValidatedReadServiceResponse
import br.com.fitnesspro.service.communication.responses.ValidatedSingleValueServiceResponse
import br.com.fitnesspro.shared.communication.constants.EndPointsV1
import br.com.fitnesspro.shared.communication.constants.Timeouts
import br.com.fitnesspro.shared.communication.paging.CommonPageInfos
import br.com.fitnesspro.shared.communication.query.filter.DeviceFilter
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
    @Transactional(timeout = Timeouts.OPERATION_MEDIUM_TIMEOUT, rollbackFor = [Exception::class], readOnly = true)
    @SecurityRequirement(name = "Bearer Authentication")
    fun getListDevice(@RequestParam filter: String, @RequestParam pageInfos: String): ResponseEntity<ValidatedReadServiceResponse<ValidatedDeviceDTO>> {
        val defaultGSon = GsonBuilder().defaultServiceGSon()
        val queryFilter = defaultGSon.fromJson(filter, DeviceFilter::class.java)
        val commonPageInfos = defaultGSon.fromJson(pageInfos, CommonPageInfos::class.java)

        val logs = deviceService.getListDevice(queryFilter, commonPageInfos)
        return ResponseEntity.ok(
            ValidatedReadServiceResponse(
                values = logs,
                code = HttpStatus.OK.value(),
                success = true
            )
        )
    }

    @GetMapping(EndPointsV1.DEVICE_COUNT)
    @Transactional(timeout = Timeouts.OPERATION_MEDIUM_TIMEOUT, rollbackFor = [Exception::class], readOnly = true)
    @SecurityRequirement(name = "Bearer Authentication")
    fun getCountListDevice(@RequestParam filter: String): ResponseEntity<ValidatedSingleValueServiceResponse<Int>> {
        val defaultGSon = GsonBuilder().defaultServiceGSon()
        val queryFilter = defaultGSon.fromJson(filter, DeviceFilter::class.java)

        val count = deviceService.getCountListDevice(queryFilter)
        return ResponseEntity.ok(
            ValidatedSingleValueServiceResponse(
                value = count,
                code = HttpStatus.OK.value(),
                success = true
            )
        )
    }

}