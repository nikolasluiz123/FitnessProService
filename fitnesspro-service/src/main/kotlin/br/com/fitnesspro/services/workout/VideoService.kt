package br.com.fitnesspro.services.workout

import br.com.fitnesspro.config.application.cache.VIDEO_IMPORT_CACHE_NAME
import br.com.fitnesspro.repository.workout.ICustomVideoRepository
import br.com.fitnesspro.repository.workout.IVideoRepository
import br.com.fitnesspro.services.mappers.VideoServiceMapper
import br.com.fitnesspro.shared.communication.dtos.workout.VideoDTO
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.WorkoutModuleImportFilter
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class VideoService(
    private val videoRepository: IVideoRepository,
    private val customVideoRepository: ICustomVideoRepository,
    private val videoServiceMapper: VideoServiceMapper
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
}