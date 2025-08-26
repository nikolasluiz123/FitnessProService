package br.com.fitnesspro.common.service.storage

import br.com.fitnesspro.common.repository.auditable.video.IVideoRepository
import br.com.fitnesspro.shared.communication.enums.storage.EnumGCBucketContentTypes
import br.com.fitnesspro.shared.communication.enums.storage.EnumGCBucketNames
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

    fun deleteVideo(videoIds: List<String>) {
        val videos = videoRepository.findAllById(videoIds)

        videos.forEach {
            deleteFile(
                bucketName = EnumGCBucketNames.VIDEO,
                fileName = it.filePath!!.substringAfterLast("/")
            )

            it.storageUrl = null
            it.storageTransmissionDate = null
        }

        videoRepository.saveAll(videos)
    }
}