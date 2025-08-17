package br.com.fitnesspro.common.controller.storage

import br.com.fitnesspro.common.service.storage.ReportGCBucketService
import br.com.fitnesspro.common.service.storage.VideoGCBucketService
import br.com.fitnesspro.log.enums.EnumRequestAttributes
import br.com.fitnesspro.shared.communication.constants.EndPointsV1
import br.com.fitnesspro.shared.communication.responses.StorageServiceResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile


@RestController
@RequestMapping(EndPointsV1.STORAGE_V1)
@Tag(name = "Storage Controller", description = "Manutenção dos Arquivos dos Buckets da Storage")
class StorageController(
    private val reportGCBucketService: ReportGCBucketService,
    private val videoGCBucketService: VideoGCBucketService
) {

    @PostMapping(EndPointsV1.UPLOAD_REPORTS, consumes = ["multipart/form-data"])
    fun uploadReports(
        @RequestParam ids: List<String>,
        @RequestParam files: List<MultipartFile>,
        request: HttpServletRequest
    ): ResponseEntity<StorageServiceResponse> {
        reportGCBucketService.uploadReport(ids, files)

        val logId = request.getAttribute(EnumRequestAttributes.LOG_ID.name) as String
        val logPackageId = request.getAttribute(EnumRequestAttributes.LOG_PACKAGE_ID.name) as String

        return ResponseEntity.ok(
            StorageServiceResponse(
                executionLogId = logId,
                executionLogPackageId = logPackageId,
                code = HttpStatus.OK.value(),
                success = true
            )
        )
    }

    @PostMapping(EndPointsV1.UPLOAD_VIDEOS, consumes = ["multipart/form-data"])
    fun uploadVideos(
        @RequestParam ids: List<String>,
        @RequestParam files: List<MultipartFile>,
        request: HttpServletRequest
    ): ResponseEntity<StorageServiceResponse> {
        videoGCBucketService.uploadReport(ids, files)

        val logId = request.getAttribute(EnumRequestAttributes.LOG_ID.name) as String
        val logPackageId = request.getAttribute(EnumRequestAttributes.LOG_PACKAGE_ID.name) as String

        return ResponseEntity.ok(
            StorageServiceResponse(
                executionLogId = logId,
                executionLogPackageId = logPackageId,
                code = HttpStatus.OK.value(),
                success = true
            )
        )
    }
}