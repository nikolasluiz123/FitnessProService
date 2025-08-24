package br.com.fitnesspro.shared.communication.dtos.serviceauth

import br.com.fitnesspro.shared.communication.dtos.serviceauth.interfaces.IServiceTokenGenerationDTO
import br.com.fitnesspro.shared.communication.enums.serviceauth.EnumTokenType

data class ServiceTokenGenerationDTO(
    override var type: EnumTokenType? = null,
    override var userId: String? = null,
    override var deviceId: String? = null,
    override var applicationId: String? = null
) : IServiceTokenGenerationDTO
