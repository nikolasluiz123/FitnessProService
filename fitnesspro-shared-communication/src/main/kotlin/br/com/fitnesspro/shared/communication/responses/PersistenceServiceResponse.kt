package br.com.fitnesspro.shared.communication.responses

import br.com.fitnesspro.shared.communication.enums.serviceauth.EnumErrorType
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Resposta padrão do serviço de persistência")
open class PersistenceServiceResponse(
    @Schema(description = "Código HTTP de resposta da requisição", required = true, example = "200")
    override var code: Int = 0,
    @Schema(description = "Indica se a requisição foi bem sucedida", required = true)
    override var success: Boolean = false,
    @Schema(description = "Mensagem de erro, caso exista", required = false)
    override var error: String? = null,
    @Schema(description = "Tipo de erro, caso exista", required = false)
    override val errorType: EnumErrorType? = null,
    val id: String? = null
): IFitnessProServiceResponse
