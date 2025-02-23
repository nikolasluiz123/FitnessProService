package br.com.fitnesspro.service.models.general

import br.com.fitnesspro.core.extensions.dateTimeNow
import br.com.fitnesspro.models.general.enums.EnumUserType
import br.com.fitnesspro.service.models.base.IntegratedModel
import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(
    name = "fitness_user",
    indexes = [
        Index(name = "idx_fitness_user_creation_user_id", columnList = "creation_user_id"),
        Index(name = "idx_fitness_user_update_user_id", columnList = "update_user_id")
    ]
)
data class User(
    @Id
    override var id: String? = UUID.randomUUID().toString(),

    override var active: Boolean = true,

    @Column(name = "creation_date", nullable = false)
    override var creationDate: LocalDateTime = dateTimeNow(),

    @Column(name = "update_date", nullable = false)
    override var updateDate: LocalDateTime = dateTimeNow(),

    @Column(name = "transmission_date", nullable = false)
    override var transmissionDate: LocalDateTime = dateTimeNow(),

    @ManyToOne
    @JoinColumn(name = "creation_user_id", nullable = false)
    override var creationUser: User? = null,

    @ManyToOne
    @JoinColumn(name = "update_user_id", nullable = false)
    override var updateUser: User? = null,

    @Column(unique = true, nullable = false, length = 64)
    var email: String? = null,

    @Column(nullable = false, length = 1024)
    private var password: String? = null,

    @Column(nullable = false)
    var type: EnumUserType? = null,

    var authenticated: Boolean = false,
): IntegratedModel(), UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(SimpleGrantedAuthority(type!!.name))
    }

    override fun getPassword(): String = password!!

    fun setPassword(password: String) {
        this.password = password
    }

    override fun getUsername(): String = email!!
}