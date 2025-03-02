package br.com.fitnesspro.service.models.general

import br.com.fitnesspro.core.extensions.dateTimeNow
import br.com.fitnesspro.service.models.base.AuditableModel
import br.com.fitnesspro.service.models.base.IntegratedModel
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(
    name = "academy",
)
data class Academy(
    @Id
    override val id: String = UUID.randomUUID().toString(),

    override var active: Boolean = true,

    @Column(name = "creation_date", nullable = false)
    override var creationDate: LocalDateTime = dateTimeNow(),

    @Column(name = "update_date", nullable = false)
    override var updateDate: LocalDateTime = dateTimeNow(),

    @Column(name = "transmission_date", nullable = false)
    override var transmissionDate: LocalDateTime = dateTimeNow(),

    @Column(nullable = false, length = 512)
    var name: String? = null,

    @Column(nullable = false, length = 512)
    var address: String? = null,

    @Column(length = 11)
    var phone: String? = null,
): IntegratedModel, AuditableModel