package br.com.fitnesspro.config.application

import br.com.fitnesspro.shared.communication.helper.HashHelper
import org.springframework.security.crypto.password.PasswordEncoder

class FitnessProPasswordEncoder: PasswordEncoder {

    override fun encode(rawPassword: CharSequence?): String {
        return HashHelper.applyHash(rawPassword.toString())
    }

    override fun matches(rawPassword: CharSequence?, encodedPassword: String?): Boolean {
        return HashHelper.applyHash(rawPassword.toString()) == encodedPassword
    }
}