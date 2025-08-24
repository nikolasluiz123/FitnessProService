package br.com.fitnesspro.shared.communication.dtos.serviceauth.interfaces

import br.com.fitnesspro.shared.communication.dtos.common.BaseDTO
import br.com.fitnesspro.shared.communication.dtos.general.interfaces.IUserDTO
import br.com.fitnesspro.shared.communication.enums.serviceauth.EnumTokenType
import java.time.LocalDateTime

interface IServiceTokenDTO : BaseDTO {
    val jwtToken: String?
    var type: EnumTokenType?
    val creationDate: LocalDateTime?
    val expirationDate: LocalDateTime?
    var user: IUserDTO?
    var device: IDeviceDTO?
    var application: IApplicationDTO?
}