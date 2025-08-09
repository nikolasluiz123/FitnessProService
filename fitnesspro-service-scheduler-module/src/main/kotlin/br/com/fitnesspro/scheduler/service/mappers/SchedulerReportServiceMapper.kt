package br.com.fitnesspro.scheduler.service.mappers

import br.com.fitnesspro.authentication.repository.auditable.IPersonRepository
import br.com.fitnesspro.common.repository.auditable.report.IReportRepository
import br.com.fitnesspro.models.general.SchedulerReport
import br.com.fitnesspro.scheduler.repository.auditable.ISchedulerReportRepository
import br.com.fitnesspro.shared.communication.dtos.general.SchedulerReportDTO
import org.springframework.stereotype.Service

@Service
class SchedulerReportServiceMapper(
    private val reportRepository: IReportRepository,
    private val schedulerReportRepository: ISchedulerReportRepository,
    private val personRepository: IPersonRepository,
) {
    fun getSchedulerReport(dto: SchedulerReportDTO): SchedulerReport {
        val schedulerReport = dto.id?.let { schedulerReportRepository.findById(it) }

        return when {
            dto.id == null -> {
                SchedulerReport(
                    person = personRepository.findById(dto.personId!!).get(),
                    report = reportRepository.findById(dto.reportId!!).get(),
                    reportContext = dto.reportContext,
                    active = dto.active
                )
            }

            schedulerReport?.isPresent == true -> {
                schedulerReport.get().copy(
                    person = personRepository.findById(dto.personId!!).get(),
                    report = reportRepository.findById(dto.reportId!!).get(),
                    reportContext = dto.reportContext,
                    active = dto.active
                )
            }

            else -> {
                SchedulerReport(
                    id = dto.id!!,
                    person = personRepository.findById(dto.personId!!).get(),
                    report = reportRepository.findById(dto.reportId!!).get(),
                    reportContext = dto.reportContext,
                    active = dto.active
                )
            }
        }
    }

    fun getSchedulerReportDTO(model: SchedulerReport): SchedulerReportDTO {
        return SchedulerReportDTO(
            id = model.id,
            creationDate = model.creationDate,
            updateDate = model.updateDate,
            personId = model.person?.id,
            reportId = model.report?.id!!,
            reportContext = model.reportContext,
            active = model.active
        )
    }
}