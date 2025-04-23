package br.com.fitnesspro.shared.communication.responses

import br.com.fitnesspro.shared.communication.dtos.serviceauth.ServiceTokenDTO
import br.com.fitnesspro.shared.communication.enums.serviceauth.EnumErrorType

data class AuthenticationServiceResponse(
    val tokens: List<ServiceTokenDTO> = emptyList(),
    override var code: Int = 0,
    override var success: Boolean = false,
    override var error: String? = null,
    override val errorType: EnumErrorType? = null
): IFitnessProServiceResponse