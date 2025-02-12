package br.com.fitnesspro.dto.common

import java.time.LocalDateTime

interface AuditableDTO : BaseDTO {
    var creationDate: LocalDateTime?
    var updateDate: LocalDateTime?
}