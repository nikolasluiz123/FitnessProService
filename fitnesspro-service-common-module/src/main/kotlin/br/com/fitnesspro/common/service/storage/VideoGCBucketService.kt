package br.com.fitnesspro.common.service.storage

import br.com.fitnesspro.common.cloud.enums.EnumGCBucketNames
import br.com.fitnesspro.common.repository.auditable.video.IVideoRepository
import br.com.fitnesspro.shared.communication.enums.storage.EnumGCBucketContentTypes
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class VideoGCBucketService(
    private val videoRepository: IVideoRepository
): AbstractGCBucketService() {

    fun uploadReport(videoIds: List<String>, files: List<MultipartFile>) {
        val storageUrls = mutableListOf<String>()

        files.parallelStream().forEach { file ->
            storageUrls.add(
                uploadFile(
                    bucketName = EnumGCBucketNames.VIDEO,
                    fileName = file.originalFilename!!,
                    contentType = EnumGCBucketContentTypes.MP4,
                    fileStream = file.inputStream
                )
            )
        }

        val videos = videoRepository.findAllById(videoIds).onEachIndexed { index, video ->
            writeDefaultFieldsStorageModelAfterUpload(video, storageUrls[index])
        }

        videoRepository.saveAll(videos)
    }
}