package br.com.fitnesspro.common.controller.storage

import br.com.fitnesspro.common.service.storage.ReportGCBucketService
import br.com.fitnesspro.common.service.storage.VideoGCBucketService
import br.com.fitnesspro.log.enums.EnumRequestAttributes
import br.com.fitnesspro.service.communication.responses.ValidatedStorageServiceResponse
import br.com.fitnesspro.shared.communication.constants.EndPointsV1
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
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
        @RequestPart ids: List<String>,
        @RequestParam files: List<MultipartFile>,
        request: HttpServletRequest
    ): ResponseEntity<ValidatedStorageServiceResponse> {
        reportGCBucketService.uploadReport(ids, files)

        val logId = request.getAttribute(EnumRequestAttributes.LOG_ID.name) as String
        val logPackageId = request.getAttribute(EnumRequestAttributes.LOG_PACKAGE_ID.name) as String

        return ResponseEntity.ok(
            ValidatedStorageServiceResponse(
                executionLogId = logId,
                executionLogPackageId = logPackageId,
                code = HttpStatus.OK.value(),
                success = true
            )
        )
    }

    @PostMapping(EndPointsV1.UPLOAD_VIDEOS, consumes = ["multipart/form-data"])
    fun uploadVideos(
        @RequestPart ids: List<String>,
        @RequestParam files: List<MultipartFile>,
        request: HttpServletRequest
    ): ResponseEntity<ValidatedStorageServiceResponse> {
        videoGCBucketService.uploadReport(ids, files)

        val logId = request.getAttribute(EnumRequestAttributes.LOG_ID.name) as String
        val logPackageId = request.getAttribute(EnumRequestAttributes.LOG_PACKAGE_ID.name) as String

        return ResponseEntity.ok(
            ValidatedStorageServiceResponse(
                executionLogId = logId,
                executionLogPackageId = logPackageId,
                code = HttpStatus.OK.value(),
                success = true
            )
        )
    }
}