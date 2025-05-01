package br.com.fitnesspro.config.application.customdetails

import br.com.fitnesspro.config.application.customdetails.authority.EnumCustomAuthorities
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class ApplicationUserDetails(private val applicationId: String) : UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(SimpleGrantedAuthority(EnumCustomAuthorities.APPLICATION_ROLE.name))
    }

    override fun getPassword(): String? {
        return null
    }

    override fun getUsername(): String {
        return applicationId
    }
}