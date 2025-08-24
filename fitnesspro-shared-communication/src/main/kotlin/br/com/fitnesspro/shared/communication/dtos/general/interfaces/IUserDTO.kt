package br.com.fitnesspro.shared.communication.dtos.general.interfaces

import br.com.fitnesspro.shared.communication.dtos.common.AuditableDTO
import br.com.fitnesspro.shared.communication.enums.general.EnumUserType

interface IUserDTO: AuditableDTO {
    var active: Boolean
    var email: String?
    var password: String?
    var type: EnumUserType?
}