package br.com.fitnesspro.workout.service

import br.com.fitnesspro.core.cache.WORKOUT_REPORT_IMPORT_CACHE_NAME
import br.com.fitnesspro.service.communication.cache.ImportationEntity
import br.com.fitnesspro.service.communication.dtos.general.ValidatedWorkoutReportDTO
import br.com.fitnesspro.shared.communication.dtos.general.interfaces.IWorkoutReportDTO
import br.com.fitnesspro.shared.communication.paging.ImportPageInfos
import br.com.fitnesspro.shared.communication.query.filter.importation.WorkoutModuleImportationFilter
import br.com.fitnesspro.workout.repository.auditable.IWorkoutReportRepository
import br.com.fitnesspro.workout.repository.jpa.ICustomWorkoutReportRepository
import br.com.fitnesspro.workout.service.mappers.WorkoutReportServiceMapper
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class WorkoutReportService(
    private val workoutReportRepository: IWorkoutReportRepository,
    private val customWorkoutReportRepository: ICustomWorkoutReportRepository,
    private val workoutReportServiceMapper: WorkoutReportServiceMapper,
) {

    @CacheEvict(cacheNames = [WORKOUT_REPORT_IMPORT_CACHE_NAME], allEntries = true)
    fun saveWorkoutReportBatch(list: List<IWorkoutReportDTO>) {
        val workoutReports = list.map {
            workoutReportServiceMapper.getWorkoutReport(it)
        }

        workoutReportRepository.saveAll(workoutReports)
    }

    @Cacheable(cacheNames = [WORKOUT_REPORT_IMPORT_CACHE_NAME], keyGenerator = "importationKeyGenerator")
    @ImportationEntity(entitySimpleName = "WorkoutReport")
    fun getWorkoutReportsImport(
        filter: WorkoutModuleImportationFilter,
        pageInfos: ImportPageInfos
    ): List<ValidatedWorkoutReportDTO> {
        return customWorkoutReportRepository.getWorkoutReportsImport(filter, pageInfos)
            .map(workoutReportServiceMapper::getValidatedWorkoutReportDTO)
    }
}