package br.com.fitnesspro.models.serviceauth

import br.com.fitnesspro.core.extensions.dateTimeNow
import br.com.fitnesspro.models.base.AuditableModel
import br.com.fitnesspro.models.general.Person
import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(
    name = "device",
    indexes = [
        Index(name = "idx_device_application_id", columnList = "application_id"),
        Index(name = "idx_device_person_id", columnList = "person_id"),
        Index(name = "idx_firebase_messaging_token", columnList = "firebase_messaging_token")
    ]
)
data class Device(
    @Id
    override val id: String = UUID.randomUUID().toString(),
    override var creationDate: LocalDateTime = dateTimeNow(),
    override var updateDate: LocalDateTime = dateTimeNow(),
    override var active: Boolean = true,

    @Column(length = 256)
    var model: String? = null,

    @Column(length = 256)
    var brand: String? = null,

    @Column(name = "android_version", length = 32)
    var androidVersion: String? = null,

    @Column(name = "firebase_messaging_token", length = 2048)
    var firebaseMessagingToken: String? = null,

    @Column(name = "zone_id")
    var zoneId: String? = null,

    @JoinColumn(name = "application_id")
    @ManyToOne
    var application: Application? = null,

    @JoinColumn(name = "person_id")
    @ManyToOne
    var person: Person? = null,
): AuditableModel
