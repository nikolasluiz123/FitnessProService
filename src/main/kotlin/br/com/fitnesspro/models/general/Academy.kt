package br.com.fitnesspro.models.general

import br.com.fitnesspro.extensions.dateTimeNow
import br.com.fitnesspro.models.base.AuditableModel
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "academy")
data class Academy(
    @Id
    override val id: String = UUID.randomUUID().toString(),

    override var active: Boolean = true,

    @Column(name = "creation_date", nullable = false)
    override var creationDate: LocalDateTime = dateTimeNow(),

    @Column(name = "update_date", nullable = false)
    override var updateDate: LocalDateTime = dateTimeNow(),

    @Column(nullable = false, length = 512)
    var name: String? = null,

    @Column(nullable = false, length = 512)
    var address: String? = null,

    @Column(length = 11)
    var phone: String? = null,
): AuditableModel()