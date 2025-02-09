package br.com.fitnesspro.models.general

import br.com.fitnesspro.models.base.BaseModel
import br.com.fitnesspro.models.general.enums.EnumUserType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

@Entity
@Table(name = "fitness_user")
data class User(
    @Id
    override var id: String? = UUID.randomUUID().toString(),
    override var active: Boolean = true,

    @Column(unique = true, nullable = false, length = 64)
    var email: String? = null,

    @Column(nullable = false, length = 1024)
    private var password: String? = null,

    @Column(nullable = false)
    var type: EnumUserType? = null,

    var authenticated: Boolean = false,
): BaseModel(), UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(SimpleGrantedAuthority(type!!.name))
    }

    override fun getPassword(): String = password!!

    fun setPassword(password: String) {
        this.password = password
    }

    override fun getUsername(): String = email!!
}