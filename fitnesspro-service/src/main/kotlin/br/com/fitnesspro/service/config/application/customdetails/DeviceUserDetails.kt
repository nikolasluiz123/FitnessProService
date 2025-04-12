package br.com.fitnesspro.service.config.application.customdetails

import br.com.fitnesspro.service.config.application.customdetails.authority.EnumCustomAuthorities
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class DeviceUserDetails(private val deviceId: String) : UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(SimpleGrantedAuthority(EnumCustomAuthorities.DEVICE_ROLE.name))
    }

    override fun getPassword(): String? {
        return null
    }

    override fun getUsername(): String {
        return deviceId
    }
}