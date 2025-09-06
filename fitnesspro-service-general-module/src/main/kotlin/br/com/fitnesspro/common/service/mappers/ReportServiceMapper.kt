package br.com.fitnesspro.common.service.mappers

import br.com.fitnesspro.common.repository.auditable.report.IReportRepository
import br.com.fitnesspro.models.general.Academy
import br.com.fitnesspro.models.general.Report
import br.com.fitnesspro.service.communication.dtos.general.ValidatedReportDTO
import br.com.fitnesspro.service.communication.extensions.getOrThrowDefaultException
import br.com.fitnesspro.shared.communication.dtos.general.interfaces.IReportDTO
import org.springframework.context.MessageSource
import org.springframework.stereotype.Service

@Service
class ReportServiceMapper(
    private val reportRepository: IReportRepository,
    private val messageSource: MessageSource
) {
    fun getReport(dto: IReportDTO): Report {
        val report = dto.id?.let { reportRepository.findById(it) }

        return when {
            dto.id == null -> {
                Report(
                    name = dto.name!!,
                    extension = dto.extension!!,
                    filePath = dto.filePath!!,
                    date = dto.date!!,
                    kbSize = dto.kbSize!!,
                    active = dto.active,
                    storageUrl = dto.storageUrl,
                    storageTransmissionDate = dto.storageTransmissionDate
                )
            }

            report?.isPresent == true -> {
                report.get().copy(
                    name = dto.name!!,
                    extension = dto.extension!!,
                    filePath = dto.filePath!!,
                    date = dto.date!!,
                    kbSize = dto.kbSize!!,
                    active = dto.active,
                    storageUrl = dto.storageUrl,
                    storageTransmissionDate = dto.storageTransmissionDate
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
                    active = dto.active,
                    storageUrl = dto.storageUrl,
                    storageTransmissionDate = dto.storageTransmissionDate
                )
            }
        }
    }

    fun getValidatedReportDTO(model: Report): ValidatedReportDTO {
        return ValidatedReportDTO(
            id = model.id,
            creationDate = model.creationDate,
            updateDate = model.updateDate,
            name = model.name,
            extension = model.extension,
            filePath = model.filePath,
            date = model.date,
            kbSize = model.kbSize,
            active = model.active,
            storageUrl = model.storageUrl,
            storageTransmissionDate = model.storageTransmissionDate
        )
    }
}