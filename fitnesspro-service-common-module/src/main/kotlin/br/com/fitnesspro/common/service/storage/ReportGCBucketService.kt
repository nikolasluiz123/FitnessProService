package br.com.fitnesspro.common.service.storage

import br.com.fitnesspro.common.cloud.enums.EnumGCBucketNames
import br.com.fitnesspro.common.repository.auditable.report.IReportRepository
import br.com.fitnesspro.core.extensions.dateTimeNow
import br.com.fitnesspro.shared.communication.enums.storage.EnumGCBucketContentTypes
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class ReportGCBucketService(
    private val reportRepository: IReportRepository,
): AbstractGCBucketService() {

    fun uploadReport(reportIds: List<String>, files: List<MultipartFile>) {
        files.parallelStream().forEach { file ->
            uploadFile(
                bucketName = EnumGCBucketNames.REPORT,
                fileName = file.originalFilename!!,
                contentType = EnumGCBucketContentTypes.PDF,
                fileStream = file.inputStream
            )
        }

        val reports = reportRepository.findAllById(reportIds).onEach {
            it.storageTransmissionDate = dateTimeNow()
        }

        reportRepository.saveAll(reports)
    }
}