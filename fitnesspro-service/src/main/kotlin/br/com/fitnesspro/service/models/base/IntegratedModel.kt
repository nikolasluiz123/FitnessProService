package br.com.fitnesspro.service.models.base

import jakarta.persistence.MappedSuperclass
import java.time.LocalDateTime

@MappedSuperclass
abstract class IntegratedModel: AuditableModel() {
    abstract var transmissionDate: LocalDateTime
}