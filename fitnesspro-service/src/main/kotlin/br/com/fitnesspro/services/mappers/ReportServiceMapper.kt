package br.com.fitnesspro.services.mappers

import br.com.fitnesspro.models.general.Report
import br.com.fitnesspro.models.general.SchedulerReport
import br.com.fitnesspro.repository.auditable.general.IPersonRepository
import br.com.fitnesspro.repository.auditable.report.IReportRepository
import br.com.fitnesspro.repository.auditable.report.ISchedulerReportRepository
import br.com.fitnesspro.shared.communication.dtos.general.ReportDTO
import br.com.fitnesspro.shared.communication.dtos.general.SchedulerReportDTO
import org.springframework.stereotype.Service

@Service
class ReportServiceMapper(
    private val reportRepository: IReportRepository,
    private val schedulerReportRepository: ISchedulerReportRepository,
    private val personRepository: IPersonRepository,
) {
    fun getReport(dto: ReportDTO): Report {
        val report = dto.id?.let { reportRepository.findById(it) }

        return when {
            dto.id == null -> {
                Report(
                    name = dto.name!!,
                    extension = dto.extension!!,
                    filePath = dto.filePath!!,
                    date = dto.date!!,
                    kbSize = dto.kbSize!!,
                    active = dto.active
                )
            }

            report?.isPresent == true -> {
                report.get().copy(
                    name = dto.name!!,
                    extension = dto.extension!!,
                    filePath = dto.filePath!!,
                    date = dto.date!!,
                    kbSize = dto.kbSize!!,
                    active = dto.active
                )
            }

            else -> {
                Report(
                    id = dto.id!!,
                    name = dto.name!!,
                    extension = dto.extension!!,
                    filePath = dto.filePath!!,
                    date = dto.date!!,
                    kbSize = dto.kbSize!!,
                    active = dto.active
                )
            }
        }
    }

    fun getReportDTO(model: Report): ReportDTO {
        return ReportDTO(
            id = model.id,
            creationDate = model.creationDate,
            updateDate = model.updateDate,
            name = model.name,
            extension = model.extension,
            filePath = model.filePath,
            date = model.date,
            kbSize = model.kbSize,
            active = model.active
        )
    }

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