package br.com.fitnesspro.shared.communication.responses

data class AuthenticationServiceResponse(
    val token: String? = null,
    override var code: Int = 0,
    override var success: Boolean = false,
    override var error: String? = null
): IFitnessProServiceResponse