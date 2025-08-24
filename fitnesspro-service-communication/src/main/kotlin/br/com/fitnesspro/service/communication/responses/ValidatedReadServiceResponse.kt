package br.com.fitnesspro.service.communication.responses

import br.com.fitnesspro.shared.communication.enums.serviceauth.EnumErrorType
import br.com.fitnesspro.shared.communication.responses.interfaces.IReadServiceResponse
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Resposta padrão do serviço de consultas")
open class ValidatedReadServiceResponse<DTO>(
    @field:Schema(description = "Lista dos resultados", required = true)
    override var values: List<DTO> = emptyList(),

    @field:Schema(description = "Código HTTP de resposta da requisição", required = true, example = "200")
    override var code: Int = 0,

    @field:Schema(description = "Indica se a requisição foi bem sucedida", required = true)
    override var success: Boolean = false,

    @field:Schema(description = "Mensagem de erro, caso exista", required = false)
    override var error: String? = null,

    @field:Schema(description = "Tipo de erro, caso exista", required = false)
    override val errorType: EnumErrorType? = null,
) : IReadServiceResponse<DTO>