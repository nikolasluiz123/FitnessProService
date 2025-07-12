package br.com.fitnesspro.services.workout

import br.com.fitnesspro.config.application.cache.VIDEO_EXERCISE_IMPORT_CACHE_NAME
import br.com.fitnesspro.config.application.cache.VIDEO_IMPORT_CACHE_NAME
import br.com.fitnesspro.repository.auditable.workout.IVideoExerciseRepository
import br.com.fitnesspro.repository.auditable.workout.IVideoRepository
import br.com.fitnesspro.repository.jpa.workout.ICustomVideoExerciseRepository
import br.com.fitnesspro.repository.jpa.workout.ICustomVideoRepository
import br.com.fitnesspro.services.mappers.VideoServiceMapper
import br.com.fitnesspro.shared.communication.dtos.workout.NewVideoExerciseDTO
import br.com.fitnesspro.shared.communication.dtos.workout.VideoDTO
import br.com.fitnesspro.shared.communication.dtos.workout.VideoExerciseDTO
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.WorkoutModuleImportFilter
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class VideoService(
    private val videoRepository: IVideoRepository,
    private val videoExerciseRepository: IVideoExerciseRepository,
    private val customVideoRepository: ICustomVideoRepository,
    private val customVideoExerciseRepository: ICustomVideoExerciseRepository,
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

    @CacheEvict(cacheNames = [VIDEO_EXERCISE_IMPORT_CACHE_NAME], allEntries = true)
    fun createVideo(newVideoExerciseDTO: NewVideoExerciseDTO) {
        val video = videoServiceMapper.getVideo(newVideoExerciseDTO.videoDTO!!)
        videoRepository.save(video)

        val videoExercise = videoServiceMapper.getVideoExercise(newVideoExerciseDTO)
        videoExerciseRepository.save(videoExercise)
    }
}