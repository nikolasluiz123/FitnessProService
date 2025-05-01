package br.com.fitnesspro.models.base

import java.time.LocalDateTime

interface AuditableModel: BaseModel {
    var creationDate: LocalDateTime
    var updateDate: LocalDateTime
}