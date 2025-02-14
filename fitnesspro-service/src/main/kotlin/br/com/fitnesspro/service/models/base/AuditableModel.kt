package br.com.fitnesspro.service.models.base

import jakarta.persistence.MappedSuperclass
import java.time.LocalDateTime

@MappedSuperclass
abstract class AuditableModel: BaseModel() {
    abstract var creationDate: LocalDateTime
    abstract var updateDate: LocalDateTime
}