package br.com.fitnesspro.shared.communication.dtos.general.interfaces

import br.com.fitnesspro.shared.communication.dtos.serviceauth.interfaces.IDeviceDTO

interface IAuthenticationDTO {
    var email: String?
    var password: String?
    var adminAuth: Boolean
    var deviceDTO: IDeviceDTO?
    var applicationJWT: String?
}