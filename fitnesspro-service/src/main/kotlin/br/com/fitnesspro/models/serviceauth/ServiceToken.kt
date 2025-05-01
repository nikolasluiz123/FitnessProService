package br.com.fitnesspro.models.serviceauth

import br.com.fitnesspro.models.general.User
import br.com.fitnesspro.shared.communication.enums.serviceauth.EnumTokenType
import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(
    name = "service_token",
    indexes = [
        Index(name = "idx_service_token_user_id", columnList = "user_id"),
        Index(name = "idx_service_token_device_id", columnList = "device_id"),
    ]
)
data class ServiceToken(
    @Id
    val id: String = UUID.randomUUID().toString(),

    @Column(name = "jwt_token", columnDefinition = "TEXT")
    val jwtToken: String? = null,

    val type: EnumTokenType? = null,

    @Column(name = "creation_date")
    val creationDate: LocalDateTime? = null,

    @Column(name = "expiration_date")
    var expirationDate: LocalDateTime? = null,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User? = null,

    @ManyToOne
    @JoinColumn(name = "device_id")
    val device: Device? = null,

    @ManyToOne
    @JoinColumn(name = "application_id")
    val application: Application? = null,
)
