package br.com.fitnesspro.service.models.serviceauth

import br.com.fitnesspro.core.extensions.dateTimeNow
import br.com.fitnesspro.service.models.base.AuditableModel
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "device")
data class Device(
    @Id
    override val id: String = UUID.randomUUID().toString(),
    override var creationDate: LocalDateTime = dateTimeNow(),
    override var updateDate: LocalDateTime = dateTimeNow(),
    override var active: Boolean = true,
    val model: String? = null
): AuditableModel
