package br.com.fitnesspro.shared.communication.dtos.serviceauth

import br.com.fitnesspro.shared.communication.dtos.general.interfaces.IUserDTO
import br.com.fitnesspro.shared.communication.dtos.serviceauth.interfaces.IApplicationDTO
import br.com.fitnesspro.shared.communication.dtos.serviceauth.interfaces.IDeviceDTO
import br.com.fitnesspro.shared.communication.dtos.serviceauth.interfaces.IServiceTokenDTO
import br.com.fitnesspro.shared.communication.enums.serviceauth.EnumTokenType
import java.time.LocalDateTime

data class ServiceTokenDTO(
    override var id: String? = null,
    override val jwtToken: String? = null,
    override var type: EnumTokenType? = null,
    override val creationDate: LocalDateTime? = null,
    override val expirationDate: LocalDateTime? = null,
    override var user: IUserDTO? = null,
    override var device: IDeviceDTO? = null,
    override var application: IApplicationDTO? = null
) : IServiceTokenDTO
