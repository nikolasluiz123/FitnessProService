package br.com.fitnesspro.shared.communication.responses

import br.com.fitnesspro.shared.communication.enums.serviceauth.EnumErrorType

data class AuthenticationServiceResponse(
    val token: String? = null,
    override var code: Int = 0,
    override var success: Boolean = false,
    override var error: String? = null,
    override val errorType: EnumErrorType? = null
): IFitnessProServiceResponse