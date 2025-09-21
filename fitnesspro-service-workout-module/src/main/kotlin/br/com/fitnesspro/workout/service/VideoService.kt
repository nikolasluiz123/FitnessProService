package br.com.fitnesspro.workout.service

import br.com.fitnesspro.common.repository.auditable.video.IVideoRepository
import br.com.fitnesspro.common.service.storage.VideoGCBucketService
import br.com.fitnesspro.core.cache.VIDEO_EXERCISE_EXECUTION_IMPORT_CACHE_NAME
import br.com.fitnesspro.core.cache.VIDEO_EXERCISE_IMPORT_CACHE_NAME
import br.com.fitnesspro.core.cache.VIDEO_EXERCISE_PRE_DEFINITION_IMPORT_CACHE_NAME
import br.com.fitnesspro.core.cache.VIDEO_IMPORT_CACHE_NAME
import br.com.fitnesspro.service.communication.dtos.workout.ValidatedVideoDTO
import br.com.fitnesspro.service.communication.dtos.workout.ValidatedVideoExerciseDTO
import br.com.fitnesspro.service.communication.dtos.workout.ValidatedVideoExerciseExecutionDTO
import br.com.fitnesspro.service.communication.dtos.workout.ValidatedVideoExercisePreDefinitionDTO
import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.IVideoDTO
import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.IVideoExerciseDTO
import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.IVideoExerciseExecutionDTO
import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.IVideoExercisePreDefinitionDTO
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.WorkoutModuleImportationFilter
import br.com.fitnesspro.workout.repository.auditable.IVideoExerciseExecutionRepository
import br.com.fitnesspro.workout.repository.auditable.IVideoExercisePreDefinitionRepository
import br.com.fitnesspro.workout.repository.auditable.IVideoExerciseRepository
import br.com.fitnesspro.workout.repository.jpa.ICustomVideoExerciseExecutionRepository
import br.com.fitnesspro.workout.repository.jpa.ICustomVideoExercisePreDefinitionRepository
import br.com.fitnesspro.workout.repository.jpa.ICustomVideoExerciseRepository
import br.com.fitnesspro.workout.repository.jpa.ICustomVideoRepository
import br.com.fitnesspro.workout.service.mappers.VideoServiceMapper
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class VideoService(
    private val videoRepository: IVideoRepository,
    private val videoExerciseRepository: IVideoExerciseRepository,
    private val videoExerciseExecutionRepository: IVideoExerciseExecutionRepository,
    private val videoExercisePreDefinitionRepository: IVideoExercisePreDefinitionRepository,
    private val customVideoRepository: ICustomVideoRepository,
    private val customVideoExerciseRepository: ICustomVideoExerciseRepository,
    private val customVideoExerciseExecutionRepository: ICustomVideoExerciseExecutionRepository,
    private val customVideoExercisePreDefinitionRepository: ICustomVideoExercisePreDefinitionRepository,
    private val videoServiceMapper: VideoServiceMapper,
    private val videoGCBucketService: VideoGCBucketService
) {

    fun getVideosImport(filter: WorkoutModuleImportationFilter, pageInfos: ImportPageInfos): List<ValidatedVideoDTO> {
        return customVideoRepository.getVideosImport(filter, pageInfos).map(videoServiceMapper::getVideoDTO)
    }

    fun saveVideoBatch(videoDTOs: List<IVideoDTO>) {
        val videos = videoDTOs.map { videoServiceMapper.getVideo(it) }
        videoRepository.saveAll(videos)

        val inactiveVideos = videos
            .filter { !it.active }
            .map { it.id }

        if (inactiveVideos.isNotEmpty()) {
            videoGCBucketService.deleteVideo(inactiveVideos)
        }
    }

    fun getVideoExercisesImport(filter: WorkoutModuleImportationFilter, pageInfos: ImportPageInfos): List<ValidatedVideoExerciseDTO> {
        return customVideoExerciseRepository.getVideoExercisesImport(filter, pageInfos).map(videoServiceMapper::getVideoExerciseDTO)
    }

    fun saveExerciseVideoBatch(exerciseVideoDTOs: List<IVideoExerciseDTO>) {
        val videos = exerciseVideoDTOs.map(videoServiceMapper::getVideoExercise)
        videoExerciseRepository.saveAll(videos)
    }

    fun getVideoExercisesExecutionImport(filter: WorkoutModuleImportationFilter, pageInfos: ImportPageInfos): List<ValidatedVideoExerciseExecutionDTO> {
        return customVideoExerciseExecutionRepository.getVideoExercisesExecutionImport(filter, pageInfos).map { video ->
            videoServiceMapper.getVideoExerciseExecutionDTO(video)
        }
    }

    fun saveExerciseExecutionVideoBatch(videoDTOs: List<IVideoExerciseExecutionDTO>) {
        val videos = videoDTOs.map(videoServiceMapper::getVideoExerciseExecution)
        videoExerciseExecutionRepository.saveAll(videos)
    }

    fun saveExercisePreDefinitionVideosBatch(exerciseVideoDTOs: List<IVideoExercisePreDefinitionDTO>) {
        val videos = exerciseVideoDTOs.map(videoServiceMapper::getVideoExercisePreDefinition)
        videoExercisePreDefinitionRepository.saveAll(videos)
    }

    fun getVideoExercisesPreDefinitionImport(filter: WorkoutModuleImportationFilter, pageInfos: ImportPageInfos): List<ValidatedVideoExercisePreDefinitionDTO> {
        return customVideoExercisePreDefinitionRepository.getVideoExercisesPreDefinitionImport(filter, pageInfos).map { video ->
            videoServiceMapper.getVideoExercisePreDefinitionDTO(video)
        }
    }
}