package br.com.fitnesspro.service.communication.gson

import br.com.fitnesspro.core.gson.*
import br.com.fitnesspro.service.communication.dtos.cache.ValidatedCacheClearConfigDTO
import br.com.fitnesspro.service.communication.dtos.cache.ValidatedCacheDTO
import br.com.fitnesspro.service.communication.dtos.cache.ValidatedCacheEntryDTO
import br.com.fitnesspro.service.communication.dtos.general.*
import br.com.fitnesspro.service.communication.dtos.logs.*
import br.com.fitnesspro.service.communication.dtos.notification.ValidatedGlobalNotificationDTO
import br.com.fitnesspro.service.communication.dtos.notification.ValidatedNotificationDTO
import br.com.fitnesspro.service.communication.dtos.scheduledtask.ValidatedScheduledTaskDTO
import br.com.fitnesspro.service.communication.dtos.scheduler.ValidatedRecurrentSchedulerDTO
import br.com.fitnesspro.service.communication.dtos.scheduler.ValidatedSchedulerConfigDTO
import br.com.fitnesspro.service.communication.dtos.scheduler.ValidatedSchedulerDTO
import br.com.fitnesspro.service.communication.dtos.serviceauth.ValidatedApplicationDTO
import br.com.fitnesspro.service.communication.dtos.serviceauth.ValidatedDeviceDTO
import br.com.fitnesspro.service.communication.dtos.serviceauth.ValidatedServiceTokenDTO
import br.com.fitnesspro.service.communication.dtos.serviceauth.ValidatedServiceTokenGenerationDTO
import br.com.fitnesspro.service.communication.dtos.workout.*
import br.com.fitnesspro.service.communication.dtos.workout.health.*
import br.com.fitnesspro.service.communication.gson.adapters.GenericInterfaceAdapterFactory
import br.com.fitnesspro.shared.communication.dtos.cache.interfaces.ICacheClearConfigDTO
import br.com.fitnesspro.shared.communication.dtos.cache.interfaces.ICacheDTO
import br.com.fitnesspro.shared.communication.dtos.cache.interfaces.ICacheEntryDTO
import br.com.fitnesspro.shared.communication.dtos.general.interfaces.*
import br.com.fitnesspro.shared.communication.dtos.logs.interfaces.*
import br.com.fitnesspro.shared.communication.dtos.notification.interfaces.IGlobalNotificationDTO
import br.com.fitnesspro.shared.communication.dtos.notification.interfaces.INotificationDTO
import br.com.fitnesspro.shared.communication.dtos.scheduledtask.IScheduledTaskDTO
import br.com.fitnesspro.shared.communication.dtos.scheduler.interfaces.IRecurrentSchedulerDTO
import br.com.fitnesspro.shared.communication.dtos.scheduler.interfaces.ISchedulerConfigDTO
import br.com.fitnesspro.shared.communication.dtos.scheduler.interfaces.ISchedulerDTO
import br.com.fitnesspro.shared.communication.dtos.serviceauth.interfaces.IApplicationDTO
import br.com.fitnesspro.shared.communication.dtos.serviceauth.interfaces.IDeviceDTO
import br.com.fitnesspro.shared.communication.dtos.serviceauth.interfaces.IServiceTokenDTO
import br.com.fitnesspro.shared.communication.dtos.serviceauth.interfaces.IServiceTokenGenerationDTO
import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.*
import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.health.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.time.*

fun GsonBuilder.defaultServiceGSon(): Gson {
    return registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeTypeAdapter())
        .registerTypeAdapter(LocalDate::class.java, LocalDateTypeAdapter())
        .registerTypeAdapter(LocalTime::class.java, LocalTimeTypeAdapter())
        .registerTypeAdapter(OffsetDateTime::class.java, OffsetDateTimeTypeAdapter())
        .registerTypeAdapter(Instant::class.java, InstantTypeAdapter())
        .registerTypeAdapter(ZoneOffset::class.java, ZoneOffsetTypeAdapter())

        .registerTypeAdapterFactory(GenericInterfaceAdapterFactory(ICacheClearConfigDTO::class.java, ValidatedCacheClearConfigDTO::class.java))
        .registerTypeAdapterFactory(GenericInterfaceAdapterFactory(ICacheDTO::class.java, ValidatedCacheDTO::class.java))
        .registerTypeAdapterFactory(GenericInterfaceAdapterFactory(ICacheEntryDTO::class.java, ValidatedCacheEntryDTO::class.java))

        .registerTypeAdapterFactory(GenericInterfaceAdapterFactory(IAcademyDTO::class.java, ValidatedAcademyDTO::class.java))
        .registerTypeAdapterFactory(GenericInterfaceAdapterFactory(IAuthenticationDTO::class.java, ValidatedAuthenticationDTO::class.java))
        .registerTypeAdapterFactory(GenericInterfaceAdapterFactory(IFindPersonDTO::class.java, ValidatedFindPersonDTO::class.java))
        .registerTypeAdapterFactory(GenericInterfaceAdapterFactory(IPersonAcademyTimeDTO::class.java,ValidatedPersonAcademyTimeDTO::class.java))
        .registerTypeAdapterFactory(GenericInterfaceAdapterFactory(IPersonDTO::class.java, ValidatedPersonDTO::class.java))
        .registerTypeAdapterFactory(GenericInterfaceAdapterFactory(IReportDTO::class.java, ValidatedReportDTO::class.java))
        .registerTypeAdapterFactory(GenericInterfaceAdapterFactory(ISchedulerReportDTO::class.java, ValidatedSchedulerReportDTO::class.java))
        .registerTypeAdapterFactory(GenericInterfaceAdapterFactory(IUserDTO::class.java, ValidatedUserDTO::class.java))

        .registerTypeAdapterFactory(GenericInterfaceAdapterFactory(IExecutionLogDTO::class.java,ValidatedExecutionLogDTO::class.java))
        .registerTypeAdapterFactory(GenericInterfaceAdapterFactory(IExecutionLogPackageDTO::class.java,ValidatedExecutionLogPackageDTO::class.java))
        .registerTypeAdapterFactory(GenericInterfaceAdapterFactory(IUpdatableExecutionLogInfosDTO::class.java,ValidatedUpdatableExecutionLogInfosDTO::class.java))
        .registerTypeAdapterFactory(GenericInterfaceAdapterFactory(IUpdatableExecutionLogPackageInfosDTO::class.java, ValidatedUpdatableExecutionLogPackageInfosDTO::class.java))
        .registerTypeAdapterFactory(GenericInterfaceAdapterFactory(IUpdatableExecutionLogSubPackageInfosDTO::class.java, ValidatedUpdatableExecutionLogSubPackageInfosDTO::class.java))
        .registerTypeAdapterFactory(GenericInterfaceAdapterFactory(IUpdatableExecutionLogSubPackageEntityCountsDTO::class.java, ValidatedUpdatableExecutionLogSubPackageEntityCountsDTO::class.java))

        .registerTypeAdapterFactory(GenericInterfaceAdapterFactory(IGlobalNotificationDTO::class.java, ValidatedGlobalNotificationDTO::class.java))
        .registerTypeAdapterFactory(GenericInterfaceAdapterFactory(INotificationDTO::class.java, ValidatedNotificationDTO::class.java))

        .registerTypeAdapterFactory(GenericInterfaceAdapterFactory(IScheduledTaskDTO::class.java, ValidatedScheduledTaskDTO::class.java))

        .registerTypeAdapterFactory(GenericInterfaceAdapterFactory(IRecurrentSchedulerDTO::class.java, ValidatedRecurrentSchedulerDTO::class.java))
        .registerTypeAdapterFactory(GenericInterfaceAdapterFactory(ISchedulerConfigDTO::class.java, ValidatedSchedulerConfigDTO::class.java))
        .registerTypeAdapterFactory(GenericInterfaceAdapterFactory(ISchedulerDTO::class.java, ValidatedSchedulerDTO::class.java))

        .registerTypeAdapterFactory(GenericInterfaceAdapterFactory(IApplicationDTO::class.java, ValidatedApplicationDTO::class.java))
        .registerTypeAdapterFactory(GenericInterfaceAdapterFactory(IDeviceDTO::class.java, ValidatedDeviceDTO::class.java))
        .registerTypeAdapterFactory(GenericInterfaceAdapterFactory(IServiceTokenDTO::class.java, ValidatedServiceTokenDTO::class.java))
        .registerTypeAdapterFactory(GenericInterfaceAdapterFactory(IServiceTokenGenerationDTO::class.java, ValidatedServiceTokenGenerationDTO::class.java))

        .registerTypeAdapterFactory(GenericInterfaceAdapterFactory(IExerciseDTO::class.java, ValidatedExerciseDTO::class.java))
        .registerTypeAdapterFactory(GenericInterfaceAdapterFactory(IExerciseExecutionDTO::class.java, ValidatedExerciseExecutionDTO::class.java))
        .registerTypeAdapterFactory(GenericInterfaceAdapterFactory(IExercisePreDefinitionDTO::class.java, ValidatedExercisePreDefinitionDTO::class.java))
        .registerTypeAdapterFactory(GenericInterfaceAdapterFactory(IVideoDTO::class.java, ValidatedVideoDTO::class.java))
        .registerTypeAdapterFactory(GenericInterfaceAdapterFactory(IVideoExerciseDTO::class.java, ValidatedVideoExerciseDTO::class.java))
        .registerTypeAdapterFactory(GenericInterfaceAdapterFactory(IVideoExerciseExecutionDTO::class.java, ValidatedVideoExerciseExecutionDTO::class.java))
        .registerTypeAdapterFactory(GenericInterfaceAdapterFactory(IVideoExercisePreDefinitionDTO::class.java, ValidatedVideoExercisePreDefinitionDTO::class.java))
        .registerTypeAdapterFactory(GenericInterfaceAdapterFactory(IWorkoutDTO::class.java, ValidatedWorkoutDTO::class.java))
        .registerTypeAdapterFactory(GenericInterfaceAdapterFactory(IWorkoutGroupDTO::class.java, ValidatedWorkoutGroupDTO::class.java))
        .registerTypeAdapterFactory(GenericInterfaceAdapterFactory(IWorkoutGroupPreDefinitionDTO::class.java,ValidatedWorkoutGroupPreDefinitionDTO::class.java))

        .registerTypeAdapterFactory(GenericInterfaceAdapterFactory(IHealthConnectMetadataDTO::class.java, ValidatedHealthConnectMetadataDTO::class.java))
        .registerTypeAdapterFactory(GenericInterfaceAdapterFactory(IHealthConnectStepsDTO::class.java, ValidatedHealthConnectStepsDTO::class.java))
        .registerTypeAdapterFactory(GenericInterfaceAdapterFactory(IHealthConnectCaloriesBurnedDTO::class.java, ValidatedHealthConnectCaloriesBurnedDTO::class.java))
        .registerTypeAdapterFactory(GenericInterfaceAdapterFactory(IHealthConnectHeartRateDTO::class.java, ValidatedHealthConnectHeartRateDTO::class.java))
        .registerTypeAdapterFactory(GenericInterfaceAdapterFactory(IHealthConnectHeartRateSamplesDTO::class.java, ValidatedHealthConnectHeartRateSamplesDTO::class.java))
        .registerTypeAdapterFactory(GenericInterfaceAdapterFactory(IHealthConnectSleepSessionDTO::class.java, ValidatedHealthConnectSleepSessionDTO::class.java))
        .registerTypeAdapterFactory(GenericInterfaceAdapterFactory(IHealthConnectSleepStagesDTO::class.java, ValidatedHealthConnectSleepStagesDTO::class.java))
        .registerTypeAdapterFactory(GenericInterfaceAdapterFactory(ISleepSessionExerciseExecutionDTO::class.java, ValidatedSleepSessionExerciseExecutionDTO::class.java))
        .create()
}