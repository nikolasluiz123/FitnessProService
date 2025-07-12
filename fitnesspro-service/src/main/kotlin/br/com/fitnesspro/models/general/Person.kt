package br.com.fitnesspro.models.general

import br.com.fitnesspro.core.extensions.dateTimeNow
import br.com.fitnesspro.models.base.AuditableModel
import br.com.fitnesspro.models.base.IntegratedModel
import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(
    name = "person",
    indexes = [
        Index(name = "idx_person_user_id", columnList = "user_id"),
    ]
)
data class Person(
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

    @Column(name = "birth_date")
    var birthDate: LocalDate? = null,

    @Column(length = 11)
    var phone: String? = null,

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    var user: User? = null
): IntegratedModel, AuditableModel