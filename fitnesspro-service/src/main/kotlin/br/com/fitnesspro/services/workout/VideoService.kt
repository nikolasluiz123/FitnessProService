package br.com.fitnesspro.services.workout

import br.com.fitnesspro.config.application.cache.VIDEO_EXERCISE_EXECUTION_IMPORT_CACHE_NAME
import br.com.fitnesspro.config.application.cache.VIDEO_EXERCISE_IMPORT_CACHE_NAME
import br.com.fitnesspro.config.application.cache.VIDEO_EXERCISE_PRE_DEFINITION_IMPORT_CACHE_NAME
import br.com.fitnesspro.config.application.cache.VIDEO_IMPORT_CACHE_NAME
import br.com.fitnesspro.models.workout.Video
import br.com.fitnesspro.models.workout.VideoExerciseExecution
import br.com.fitnesspro.repository.auditable.workout.IVideoExerciseExecutionRepository
import br.com.fitnesspro.repository.auditable.workout.IVideoExercisePreDefinitionRepository
import br.com.fitnesspro.repository.auditable.workout.IVideoExerciseRepository
import br.com.fitnesspro.repository.auditable.workout.IVideoRepository
import br.com.fitnesspro.repository.jpa.workout.ICustomVideoExerciseExecutionRepository
import br.com.fitnesspro.repository.jpa.workout.ICustomVideoExercisePreDefinitionRepository
import br.com.fitnesspro.repository.jpa.workout.ICustomVideoExerciseRepository
import br.com.fitnesspro.repository.jpa.workout.ICustomVideoRepository
import br.com.fitnesspro.services.mappers.VideoServiceMapper
import br.com.fitnesspro.shared.communication.dtos.workout.*
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.WorkoutModuleImportFilter
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
) {

    @Cacheable(cacheNames = [VIDEO_IMPORT_CACHE_NAME], key = "#filter.toCacheKey()")
    fun getVideosImport(filter: WorkoutModuleImportFilter, pageInfos: ImportPageInfos): List<VideoDTO> {
        return customVideoRepository.getVideosImport(filter, pageInfos).map(videoServiceMapper::getVideoDTO)
    }

    @CacheEvict(cacheNames = [VIDEO_IMPORT_CACHE_NAME], allEntries = true)
    fun saveVideoBatch(videoDTOs: List<VideoDTO>) {
        val videos = videoDTOs.map { videoServiceMapper.getVideo(it) }
        videoRepository.saveAll(videos)
    }

    @Cacheable(cacheNames = [VIDEO_EXERCISE_IMPORT_CACHE_NAME], key = "#filter.toCacheKey()")
    fun getVideoExercisesImport(filter: WorkoutModuleImportFilter, pageInfos: ImportPageInfos): List<VideoExerciseDTO> {
        return customVideoExerciseRepository.getVideoExercisesImport(filter, pageInfos).map(videoServiceMapper::getVideoExerciseDTO)
    }

    @CacheEvict(cacheNames = [VIDEO_EXERCISE_IMPORT_CACHE_NAME], allEntries = true)
    fun saveExerciseVideoBatch(exerciseVideoDTOs: List<VideoExerciseDTO>) {
        val videos = exerciseVideoDTOs.map(videoServiceMapper::getVideoExercise)
        videoExerciseRepository.saveAll(videos)
    }

    @CacheEvict(cacheNames = [VIDEO_EXERCISE_EXECUTION_IMPORT_CACHE_NAME], allEntries = true)
    fun createExerciseExecutionVideos(videosDTO: List<NewVideoExerciseExecutionDTO>) {
        if (videosDTO.isNotEmpty()) {
            val videos = mutableListOf<Video>()
            val videosExerciseExecution = mutableListOf<VideoExerciseExecution>()

            videosDTO.forEach {
                videos.add(videoServiceMapper.getVideo(it.videoDTO!!))
                videosExerciseExecution.add(videoServiceMapper.getVideoExerciseExecution(it))
            }

            videoRepository.saveAll(videos)
            videoExerciseExecutionRepository.saveAll(videosExerciseExecution)
        }
    }

    @Cacheable(cacheNames = [VIDEO_EXERCISE_EXECUTION_IMPORT_CACHE_NAME], key = "#filter.toCacheKey()")
    fun getVideoExercisesExecutionImport(filter: WorkoutModuleImportFilter, pageInfos: ImportPageInfos): List<VideoExerciseExecutionDTO> {
        return customVideoExerciseExecutionRepository.getVideoExercisesExecutionImport(filter, pageInfos).map { video ->
            videoServiceMapper.getVideoExerciseExecutionDTO(video)
        }
    }

    @CacheEvict(cacheNames = [VIDEO_EXERCISE_EXECUTION_IMPORT_CACHE_NAME], allEntries = true)
    fun saveExerciseExecutionVideoBatch(videoDTOs: List<VideoExerciseExecutionDTO>) {
        val videos = videoDTOs.map(videoServiceMapper::getVideoExerciseExecution)
        videoExerciseExecutionRepository.saveAll(videos)
    }

    @CacheEvict(cacheNames = [VIDEO_EXERCISE_PRE_DEFINITION_IMPORT_CACHE_NAME], allEntries = true)
    fun saveExercisePreDefinitionVideosBatch(exerciseVideoDTOs: List<VideoExercisePreDefinitionDTO>) {
        val videos = exerciseVideoDTOs.map(videoServiceMapper::getVideoExercisePreDefinition)
        videoExercisePreDefinitionRepository.saveAll(videos)
    }

    @Cacheable(cacheNames = [VIDEO_EXERCISE_PRE_DEFINITION_IMPORT_CACHE_NAME], key = "#filter.toCacheKey()")
    fun getVideoExercisesPreDefinitionImport(filter: WorkoutModuleImportFilter, pageInfos: ImportPageInfos): List<VideoExercisePreDefinitionDTO> {
        return customVideoExercisePreDefinitionRepository.getVideoExercisesPreDefinitionImport(filter, pageInfos).map { video ->
            videoServiceMapper.getVideoExercisePreDefinitionDTO(video)
        }
    }
}