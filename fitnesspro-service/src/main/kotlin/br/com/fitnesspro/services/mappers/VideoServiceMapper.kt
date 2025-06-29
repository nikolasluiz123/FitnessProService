package br.com.fitnesspro.services.mappers

import br.com.fitnesspro.models.workout.Video
import br.com.fitnesspro.repository.workout.IVideoRepository
import br.com.fitnesspro.shared.communication.dtos.workout.VideoDTO
import org.springframework.stereotype.Service

@Service
class VideoServiceMapper(
    private val videoRepository: IVideoRepository
) {

    fun getVideoDTO(model: Video): VideoDTO {
        return VideoDTO(
            id = model.id,
            creationDate = model.creationDate,
            updateDate = model.updateDate,
            active = model.active,
            extension = model.extension,
            filePath = model.filePath,
            date = model.date,
            kbSize = model.kbSize,
            seconds = model.seconds,
            width = model.width,
            height = model.height
        )
    }

    fun getVideo(dto: VideoDTO): Video {
        val video = dto.id?.let { videoRepository.findById(it) }

        return when {
            dto.id == null -> {
                Video(
                    active = dto.active,
                    extension = dto.extension!!,
                    filePath = dto.filePath!!,
                    date = dto.date!!,
                    kbSize = dto.kbSize!!,
                    seconds = dto.seconds!!,
                    width = dto.width!!,
                    height = dto.height!!
                )
            }

            video?.isPresent == true -> {
                video.get().copy(
                    active = dto.active,
                    extension = dto.extension!!,
                    filePath = dto.filePath!!,
                    date = dto.date!!,
                    kbSize = dto.kbSize!!,
                    seconds = dto.seconds!!,
                    width = dto.width!!,
                    height = dto.height!!
                )
            }

            else -> {
                Video(
                    id = dto.id!!,
                    active = dto.active,
                    extension = dto.extension!!,
                    filePath = dto.filePath!!,
                    date = dto.date!!,
                    kbSize = dto.kbSize!!,
                    seconds = dto.seconds,
                    width = dto.width!!,
                    height = dto.height!!
                )
            }
        }
    }
}