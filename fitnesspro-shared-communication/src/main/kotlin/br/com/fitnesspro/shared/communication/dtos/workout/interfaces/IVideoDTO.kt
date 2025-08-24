package br.com.fitnesspro.shared.communication.dtos.workout.interfaces

import br.com.fitnesspro.shared.communication.dtos.common.AuditableDTO
import br.com.fitnesspro.shared.communication.dtos.common.StorageModelDTO
import java.time.LocalDateTime

interface IVideoDTO : AuditableDTO, StorageModelDTO {
    var active: Boolean
    var extension: String?
    var filePath: String?
    var date: LocalDateTime?
    var kbSize: Long?
    var seconds: Long?
    var width: Int?
    var height: Int?
}