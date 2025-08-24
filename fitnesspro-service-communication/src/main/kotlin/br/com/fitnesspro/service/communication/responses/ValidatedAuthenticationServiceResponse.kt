package br.com.fitnesspro.service.communication.responses

import br.com.fitnesspro.service.communication.dtos.serviceauth.ValidatedServiceTokenDTO
import br.com.fitnesspro.shared.communication.enums.serviceauth.EnumErrorType
import br.com.fitnesspro.shared.communication.responses.interfaces.IAuthenticationServiceResponse
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Resposta utilizada nos endpoints de autenticação")
data class ValidatedAuthenticationServiceResponse(
    @field:Schema(description = "Lista dos tokens gerados para o usuário que se autenticou", required = true)
    override val tokens: List<ValidatedServiceTokenDTO> = emptyList(),

    @field:Schema(description = "Código HTTP de resposta da requisição", required = true, example = "200")
    override var code: Int = 0,

    @field:Schema(description = "Indica se a requisição foi bem sucedida", required = true)
    override var success: Boolean = false,

    @field:Schema(description = "Mensagem de erro, caso exista", required = false)
    override var error: String? = null,

    @field:Schema(description = "Tipo de erro, caso exista", required = false)
    override val errorType: EnumErrorType? = null,
): IAuthenticationServiceResponse