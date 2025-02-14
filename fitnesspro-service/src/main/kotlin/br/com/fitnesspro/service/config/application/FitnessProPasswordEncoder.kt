package br.com.fitnesspro.service.config.application

import br.com.fitnesspro.service.helper.HashHelper
import org.springframework.security.crypto.password.PasswordEncoder

class FitnessProPasswordEncoder: PasswordEncoder {

    override fun encode(rawPassword: CharSequence?): String {
        return HashHelper.applyHash(rawPassword.toString())
    }

    override fun matches(rawPassword: CharSequence?, encodedPassword: String?): Boolean {
        return HashHelper.applyHash(rawPassword.toString()) == encodedPassword
    }
}