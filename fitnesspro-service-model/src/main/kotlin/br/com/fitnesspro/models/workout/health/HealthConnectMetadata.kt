package br.com.fitnesspro.models.workout.health

import br.com.fitnesspro.core.extensions.dateTimeNow
import br.com.fitnesspro.models.base.AuditableModel
import br.com.fitnesspro.models.base.IntegratedModel
import br.com.fitnesspro.shared.communication.enums.health.EnumRecordingMethod
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "health_connect_metadata")
data class HealthConnectMetadata(
    @Id
    override val id: String,

    @Column(nullable = false)
    override var active: Boolean = true,

    @Column(name = "creation_date", nullable = false)
    override var creationDate: LocalDateTime = dateTimeNow(),

    @Column(name = "update_date", nullable = false)
    override var updateDate: LocalDateTime = dateTimeNow(),

    @Column(name = "transmission_date", nullable = false)
    override var transmissionDate: LocalDateTime = dateTimeNow(),

    @Column(name = "data_origin_package")
    var dataOriginPackage: String? = null,

    @Column(name = "last_modified_time")
    var lastModifiedTime: LocalDateTime? = null,

    @Column(name = "client_record_id")
    var clientRecordId: String? = null,

    @Column(name = "device_manufacturer")
    var deviceManufacturer: String? = null,

    @Column(name = "device_model")
    var deviceModel: String? = null,

    @Column(name = "recording_method")
    var recordingMethod: EnumRecordingMethod? = null
) : IntegratedModel, AuditableModel