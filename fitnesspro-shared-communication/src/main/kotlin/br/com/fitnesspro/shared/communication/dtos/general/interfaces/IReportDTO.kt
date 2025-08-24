package br.com.fitnesspro.shared.communication.dtos.general.interfaces

import br.com.fitnesspro.shared.communication.dtos.common.AuditableDTO
import br.com.fitnesspro.shared.communication.dtos.common.StorageModelDTO
import java.time.LocalDateTime

interface IReportDTO: AuditableDTO, StorageModelDTO {
    var name: String?
    var extension: String?
    var filePath: String?
    var date: LocalDateTime?
    var kbSize: Long?
    var active: Boolean
}