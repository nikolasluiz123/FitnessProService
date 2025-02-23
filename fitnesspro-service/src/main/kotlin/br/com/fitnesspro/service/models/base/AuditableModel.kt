package br.com.fitnesspro.service.models.base

import br.com.fitnesspro.service.models.general.User
import jakarta.persistence.MappedSuperclass
import java.time.LocalDateTime

@MappedSuperclass
abstract class AuditableModel: BaseModel() {
    abstract var creationDate: LocalDateTime
    abstract var updateDate: LocalDateTime
    abstract var creationUser: User?
    abstract var updateUser: User?
}