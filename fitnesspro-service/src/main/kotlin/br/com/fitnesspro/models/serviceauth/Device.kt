package br.com.fitnesspro.models.serviceauth

import br.com.fitnesspro.core.extensions.dateTimeNow
import br.com.fitnesspro.models.base.AuditableModel
import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(
    name = "device",
    indexes = [
        Index(name = "idx_device_application_id", columnList = "application_id")
    ]
)
data class Device(
    @Id
    override val id: String = UUID.randomUUID().toString(),
    override var creationDate: LocalDateTime = dateTimeNow(),
    override var updateDate: LocalDateTime = dateTimeNow(),
    override var active: Boolean = true,
    @Column(length = 256)
    val model: String? = null,
    @Column(length = 256)
    val brand: String? = null,
    @Column(name = "android_version", length = 32)
    val androidVersion: String? = null,
    @JoinColumn(name = "application_id")
    @ManyToOne
    val application: Application? = null
): AuditableModel
