package br.com.fitnesspro.models.workout

import br.com.fitnesspro.core.extensions.dateTimeNow
import br.com.fitnesspro.models.base.AuditableModel
import br.com.fitnesspro.models.base.IntegratedModel
import br.com.fitnesspro.models.base.StorageModel
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "video")
data class Video(
    @Id
    override val id: String = UUID.randomUUID().toString(),

    @Column(name = "transmission_date", nullable = false)
    override var transmissionDate: LocalDateTime = dateTimeNow(),

    @Column(name = "storage_transmission_date")
    override var storageTransmissionDate: LocalDateTime? = null,

    @Column(name = "creation_date", nullable = false)
    override var creationDate: LocalDateTime = dateTimeNow(),

    @Column(name = "update_date", nullable = false)
    override var updateDate: LocalDateTime = dateTimeNow(),

    @Column(nullable = false)
    override var active: Boolean = true,

    @Column(nullable = false, length = 8)
    var extension: String? = null,

    @Column(name = "file_path", nullable = false, length = 512)
    var filePath: String? = null,

    @Column(nullable = false)
    var date: LocalDateTime = dateTimeNow(),

    @Column(name = "kb_size", nullable = false)
    var kbSize: Long? = null,

    @Column(nullable = false)
    var seconds: Long? = null,

    @Column(nullable = false)
    var width: Int? = null,

    @Column(nullable = false)
    var height: Int? = null
) : IntegratedModel, AuditableModel, StorageModel