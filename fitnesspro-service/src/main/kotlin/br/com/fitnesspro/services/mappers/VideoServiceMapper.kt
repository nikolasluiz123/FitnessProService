package br.com.fitnesspro.services.mappers

import br.com.fitnesspro.models.workout.Video
import br.com.fitnesspro.models.workout.VideoExercise
import br.com.fitnesspro.repository.workout.IExerciseRepository
import br.com.fitnesspro.repository.workout.IVideoExerciseRepository
import br.com.fitnesspro.repository.workout.IVideoRepository
import br.com.fitnesspro.shared.communication.dtos.workout.NewVideoExerciseDTO
import br.com.fitnesspro.shared.communication.dtos.workout.VideoDTO
import br.com.fitnesspro.shared.communication.dtos.workout.VideoExerciseDTO
import org.springframework.stereotype.Service

@Service
class VideoServiceMapper(
    private val videoRepository: IVideoRepository,
    private val videoExerciseRepository: IVideoExerciseRepository,
    private val exerciseRepository: IExerciseRepository
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

    fun getVideoExerciseDTO(model: VideoExercise): VideoExerciseDTO {
        return VideoExerciseDTO(
            id = model.id,
            creationDate = model.creationDate,
            updateDate = model.updateDate,
            active = model.active,
            exerciseId = model.exercise?.id!!,
            videoId = model.video?.id!!
        )
    }

    fun getVideoExercise(dto: VideoExerciseDTO): VideoExercise {
        val video = dto.id?.let { videoExerciseRepository.findById(it) }

        return when {
            dto.id == null -> {
                VideoExercise(
                    active = dto.active,
                    exercise = exerciseRepository.findById(dto.exerciseId!!).get(),
                    video = videoRepository.findById(dto.videoId!!).get()
                )
            }

            video?.isPresent == true -> {
                video.get().copy(
                    active = dto.active
                )
            }

            else -> {
                VideoExercise(
                    id = dto.id!!,
                    active = dto.active,
                    exercise = exerciseRepository.findById(dto.exerciseId!!).get(),
                    video = videoRepository.findById(dto.videoId!!).get()
                )
            }
        }
    }

    fun getVideoExercise(dto: NewVideoExerciseDTO): VideoExercise {
        val video = dto.id?.let { videoExerciseRepository.findById(it) }

        return when {
            dto.id == null -> {
                VideoExercise(
                    active = dto.active,
                    exercise = exerciseRepository.findById(dto.exerciseId!!).get(),
                    video = videoRepository.findById(dto.videoDTO?.id!!).get()
                )
            }

            video?.isPresent == true -> {
                video.get().copy(
                    active = dto.active
                )
            }

            else -> {
                VideoExercise(
                    id = dto.id!!,
                    active = dto.active,
                    exercise = exerciseRepository.findById(dto.exerciseId!!).get(),
                    video = videoRepository.findById(dto.videoDTO?.id!!).get()
                )
            }
        }
    }
}