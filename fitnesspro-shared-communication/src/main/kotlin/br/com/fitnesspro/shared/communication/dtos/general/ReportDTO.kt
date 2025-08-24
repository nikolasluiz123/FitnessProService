package br.com.fitnesspro.shared.communication.dtos.general

import br.com.fitnesspro.shared.communication.dtos.general.interfaces.IReportDTO
import java.time.LocalDateTime

data class ReportDTO(
    override var id: String? = null,
    override var creationDate: LocalDateTime? = null,
    override var updateDate: LocalDateTime? = null,
    override var storageTransmissionDate: LocalDateTime? = null,
    override var storageUrl: String? = null,
    override var name: String? = null,
    override var extension: String? = null,
    override var filePath: String? = null,
    override var date: LocalDateTime? = null,
    override var kbSize: Long? = null,
    override var active: Boolean = true,
): IReportDTO