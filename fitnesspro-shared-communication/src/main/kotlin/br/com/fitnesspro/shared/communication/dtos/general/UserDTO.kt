package br.com.fitnesspro.shared.communication.dtos.general

import br.com.fitnesspro.shared.communication.dtos.general.interfaces.IUserDTO
import br.com.fitnesspro.shared.communication.enums.general.EnumUserType
import java.time.LocalDateTime

data class UserDTO(
    override var id: String? = null,
    override var creationDate: LocalDateTime? = null,
    override var updateDate: LocalDateTime? = null,
    override var active: Boolean = true,
    override var email: String? = null,
    override var password: String? = null,
    override var type: EnumUserType? = null,
): IUserDTO