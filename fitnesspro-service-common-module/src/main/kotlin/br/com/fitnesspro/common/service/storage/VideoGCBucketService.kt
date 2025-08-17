package br.com.fitnesspro.common.service.storage

import br.com.fitnesspro.shared.communication.enums.storage.EnumGCBucketContentTypes
import br.com.fitnesspro.common.cloud.enums.EnumGCBucketNames
import br.com.fitnesspro.common.repository.auditable.video.IVideoRepository
import br.com.fitnesspro.core.extensions.dateTimeNow
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class VideoGCBucketService(
    private val videoRepository: IVideoRepository
): AbstractGCBucketService() {

    fun uploadReport(videoIds: List<String>, files: List<MultipartFile>) {
        files.parallelStream().forEach { file ->
            uploadFile(
                bucketName = EnumGCBucketNames.VIDEO,
                fileName = file.originalFilename!!,
                contentType = EnumGCBucketContentTypes.MP4,
                fileStream = file.inputStream
            )
        }

        val videos = videoRepository.findAllById(videoIds).onEach {
            it.storageTransmissionDate = dateTimeNow()
        }

        videoRepository.saveAll(videos)
    }
}