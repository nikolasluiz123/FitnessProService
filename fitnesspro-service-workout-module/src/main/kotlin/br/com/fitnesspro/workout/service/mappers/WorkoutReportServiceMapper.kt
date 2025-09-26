package br.com.fitnesspro.workout.service.mappers

import br.com.fitnesspro.authentication.repository.auditable.IPersonRepository
import br.com.fitnesspro.common.repository.auditable.report.IReportRepository
import br.com.fitnesspro.models.general.Person
import br.com.fitnesspro.models.general.Report
import br.com.fitnesspro.models.general.WorkoutReport
import br.com.fitnesspro.models.workout.Workout
import br.com.fitnesspro.service.communication.dtos.general.ValidatedWorkoutReportDTO
import br.com.fitnesspro.service.communication.extensions.getOrThrowDefaultException
import br.com.fitnesspro.shared.communication.dtos.general.interfaces.IWorkoutReportDTO
import br.com.fitnesspro.workout.repository.auditable.IWorkoutReportRepository
import br.com.fitnesspro.workout.repository.auditable.IWorkoutRepository
import org.springframework.context.MessageSource
import org.springframework.stereotype.Service

@Service
class WorkoutReportServiceMapper(
    private val reportRepository: IReportRepository,
    private val workoutReportRepository: IWorkoutReportRepository,
    private val workoutRepository: IWorkoutRepository,
    private val personRepository: IPersonRepository,
    private val messageSource: MessageSource
) {
    fun getWorkoutReport(dto: IWorkoutReportDTO): WorkoutReport {
        val workoutReport = dto.id?.let { workoutReportRepository.findById(it) }

        return when {
            dto.id == null -> {
                WorkoutReport(
                    person = personRepository.findById(dto.personId!!).getOrThrowDefaultException(messageSource, Person::class),
                    report = reportRepository.findById(dto.reportId!!).getOrThrowDefaultException(messageSource, Report::class),
                    workout = workoutRepository.findById(dto.workoutId!!).getOrThrowDefaultException(messageSource, Workout::class),
                    reportContext = dto.reportContext,
                    active = dto.active
                )
            }

            workoutReport?.isPresent == true -> {
                workoutReport.get().copy(
                    person = personRepository.findById(dto.personId!!).getOrThrowDefaultException(messageSource, Person::class),
                    report = reportRepository.findById(dto.reportId!!).getOrThrowDefaultException(messageSource, Report::class),
                    workout = workoutRepository.findById(dto.workoutId!!).getOrThrowDefaultException(messageSource, Workout::class),
                    reportContext = dto.reportContext,
                    active = dto.active
                )
            }

            else -> {
                WorkoutReport(
                    id = dto.id!!,
                    person = personRepository.findById(dto.personId!!).getOrThrowDefaultException(messageSource, Person::class),
                    report = reportRepository.findById(dto.reportId!!).getOrThrowDefaultException(messageSource, Report::class),
                    workout = workoutRepository.findById(dto.workoutId!!).getOrThrowDefaultException(messageSource, Workout::class),
                    reportContext = dto.reportContext,
                    active = dto.active
                )
            }
        }
    }

    fun getValidatedWorkoutReportDTO(model: WorkoutReport): ValidatedWorkoutReportDTO {
        return ValidatedWorkoutReportDTO(
            id = model.id,
            creationDate = model.creationDate,
            updateDate = model.updateDate,
            personId = model.person?.id,
            reportId = model.report?.id!!,
            workoutId = model.workout?.id!!,
            reportContext = model.reportContext,
            active = model.active
        )
    }
}