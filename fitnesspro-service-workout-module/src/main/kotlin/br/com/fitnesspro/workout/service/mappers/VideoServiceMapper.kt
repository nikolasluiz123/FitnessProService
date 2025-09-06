package br.com.fitnesspro.workout.service.mappers

import br.com.fitnesspro.common.repository.auditable.video.IVideoRepository
import br.com.fitnesspro.models.workout.Exercise
import br.com.fitnesspro.models.workout.ExerciseExecution
import br.com.fitnesspro.models.workout.ExercisePreDefinition
import br.com.fitnesspro.models.workout.Video
import br.com.fitnesspro.models.workout.VideoExercise
import br.com.fitnesspro.models.workout.VideoExerciseExecution
import br.com.fitnesspro.models.workout.VideoExercisePreDefinition
import br.com.fitnesspro.service.communication.dtos.workout.ValidatedVideoDTO
import br.com.fitnesspro.service.communication.dtos.workout.ValidatedVideoExerciseDTO
import br.com.fitnesspro.service.communication.dtos.workout.ValidatedVideoExerciseExecutionDTO
import br.com.fitnesspro.service.communication.dtos.workout.ValidatedVideoExercisePreDefinitionDTO
import br.com.fitnesspro.service.communication.extensions.getOrThrowDefaultException
import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.IVideoDTO
import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.IVideoExerciseDTO
import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.IVideoExerciseExecutionDTO
import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.IVideoExercisePreDefinitionDTO
import br.com.fitnesspro.workout.repository.auditable.*
import org.springframework.context.MessageSource
import org.springframework.stereotype.Service

@Service
class VideoServiceMapper(
    private val videoRepository: IVideoRepository,
    private val videoExerciseRepository: IVideoExerciseRepository,
    private val videoExerciseExecutionRepository: IVideoExerciseExecutionRepository,
    private val videoExercisePreDefinitionRepository: IVideoExercisePreDefinitionRepository,
    private val exerciseRepository: IExerciseRepository,
    private val exerciseExecutionRepository: IExerciseExecutionRepository,
    private val exercisePreDefinitionRepository: IExercisePreDefinitionRepository,
    private val messageSource: MessageSource
) {

    fun getVideoDTO(model: Video): ValidatedVideoDTO {
        return ValidatedVideoDTO(
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
            height = model.height,
            storageUrl = model.storageUrl,
            storageTransmissionDate = model.storageTransmissionDate
        )
    }

    fun getVideo(dto: IVideoDTO): Video {
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
                    height = dto.height!!,
                    storageUrl = dto.storageUrl,
                    storageTransmissionDate = dto.storageTransmissionDate
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
                    height = dto.height!!,
                    storageUrl = dto.storageUrl,
                    storageTransmissionDate = dto.storageTransmissionDate
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
                    height = dto.height!!,
                    storageUrl = dto.storageUrl,
                    storageTransmissionDate = dto.storageTransmissionDate
                )
            }
        }
    }

    fun getVideoExerciseDTO(model: VideoExercise): ValidatedVideoExerciseDTO {
        return ValidatedVideoExerciseDTO(
            id = model.id,
            creationDate = model.creationDate,
            updateDate = model.updateDate,
            active = model.active,
            exerciseId = model.exercise?.id!!,
            videoId = model.video?.id!!
        )
    }

    fun getVideoExercise(dto: IVideoExerciseDTO): VideoExercise {
        val video = dto.id?.let { videoExerciseRepository.findById(it) }

        return when {
            dto.id == null -> {
                VideoExercise(
                    active = dto.active,
                    exercise = exerciseRepository.findById(dto.exerciseId!!).getOrThrowDefaultException(messageSource, Exercise::class),
                    video = videoRepository.findById(dto.videoId!!).getOrThrowDefaultException(messageSource, Video::class)
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
                    exercise = exerciseRepository.findById(dto.exerciseId!!).getOrThrowDefaultException(messageSource, Exercise::class),
                    video = videoRepository.findById(dto.videoId!!).getOrThrowDefaultException(messageSource, Video::class)
                )
            }
        }
    }

    fun getVideoExerciseExecution(dto: IVideoExerciseExecutionDTO): VideoExerciseExecution {
        val video = dto.id?.let { videoExerciseExecutionRepository.findById(it) }

        return when {
            dto.id == null -> {
                VideoExerciseExecution(
                    active = dto.active,
                    exerciseExecution = exerciseExecutionRepository.findById(dto.exerciseExecutionId!!).getOrThrowDefaultException(messageSource, ExerciseExecution::class),
                    video = videoRepository.findById(dto.videoId!!).getOrThrowDefaultException(messageSource, Video::class)
                )
            }

            video?.isPresent == true -> {
                video.get().copy(
                    active = dto.active,
                )
            }

            else -> {
                VideoExerciseExecution(
                    id = dto.id!!,
                    active = dto.active,
                    exerciseExecution = exerciseExecutionRepository.findById(dto.exerciseExecutionId!!).getOrThrowDefaultException(messageSource, ExerciseExecution::class),
                    video = videoRepository.findById(dto.videoId!!).getOrThrowDefaultException(messageSource, Video::class)
                )
            }
        }
    }

    fun getVideoExerciseExecutionDTO(model: VideoExerciseExecution): ValidatedVideoExerciseExecutionDTO {
        return ValidatedVideoExerciseExecutionDTO(
            id = model.id,
            creationDate = model.creationDate,
            updateDate = model.updateDate,
            active = model.active,
            exerciseExecutionId = model.exerciseExecution?.id!!,
            videoId = model.video?.id!!
        )
    }

    fun getVideoExercisePreDefinition(dto: IVideoExercisePreDefinitionDTO): VideoExercisePreDefinition {
        val video = dto.id?.let { videoExercisePreDefinitionRepository.findById(it) }

        return when {
            dto.id == null -> {
                VideoExercisePreDefinition(
                    active = dto.active,
                    exercisePreDefinition = exercisePreDefinitionRepository.findById(dto.exercisePreDefinitionId!!).getOrThrowDefaultException(messageSource, ExercisePreDefinition::class),
                    video = videoRepository.findById(dto.videoId!!).getOrThrowDefaultException(messageSource, Video::class)
                )
            }

            video?.isPresent == true -> {
                video.get().copy(
                    active = dto.active,
                )
            }

            else -> {
                VideoExercisePreDefinition(
                    id = dto.id!!,
                    active = dto.active,
                    exercisePreDefinition = exercisePreDefinitionRepository.findById(dto.exercisePreDefinitionId!!).getOrThrowDefaultException(messageSource, ExercisePreDefinition::class),
                    video = videoRepository.findById(dto.videoId!!).getOrThrowDefaultException(messageSource, Video::class)
                )
            }
        }
    }

    fun getVideoExercisePreDefinitionDTO(model: VideoExercisePreDefinition): ValidatedVideoExercisePreDefinitionDTO {
        return ValidatedVideoExercisePreDefinitionDTO(
            id = model.id,
            creationDate = model.creationDate,
            updateDate = model.updateDate,
            active = model.active,
            exercisePreDefinitionId = model.exercisePreDefinition?.id!!,
            videoId = model.video?.id!!
        )
    }
}