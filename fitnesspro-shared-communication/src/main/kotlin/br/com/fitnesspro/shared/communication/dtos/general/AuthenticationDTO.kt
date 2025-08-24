package br.com.fitnesspro.shared.communication.dtos.general

import br.com.fitnesspro.shared.communication.dtos.general.interfaces.IAuthenticationDTO
import br.com.fitnesspro.shared.communication.dtos.serviceauth.interfaces.IDeviceDTO

data class AuthenticationDTO(
    override var email: String? = null,
    override var password: String? = null,
    override var adminAuth: Boolean = false,
    override var deviceDTO: IDeviceDTO? = null,
    override var applicationJWT: String? = null
): IAuthenticationDTO