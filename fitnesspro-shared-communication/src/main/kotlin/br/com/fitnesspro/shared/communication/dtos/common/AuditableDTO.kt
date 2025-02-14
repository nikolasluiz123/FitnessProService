package br.com.fitnesspro.shared.communication.dtos.common

import java.time.LocalDateTime

interface AuditableDTO : BaseDTO {
    var creationDate: LocalDateTime?
    var updateDate: LocalDateTime?
}