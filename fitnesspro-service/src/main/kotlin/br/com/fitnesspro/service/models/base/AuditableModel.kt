package br.com.fitnesspro.service.models.base

import br.com.fitnesspro.service.models.general.User
import java.time.LocalDateTime

interface AuditableModel: BaseModel {
    var creationDate: LocalDateTime
    var updateDate: LocalDateTime
    var creationUser: User?
    var updateUser: User?
}