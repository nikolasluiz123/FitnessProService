package br.com.fitnesspro.shared.communication.responses

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "Resposta padrão do serviço de persistência")
data class PersistenceServiceResponse(
    @Schema(description = "Código HTTP de resposta da requisição", required = true, example = "200")
    override var code: Int = 0,
    @Schema(description = "Indica se a requisição foi bem sucedida", required = true)
    override var success: Boolean = false,
    @Schema(description = "Mensagem de erro, caso exista", required = false)
    override var error: String? = null,
    var transmissionDate: LocalDateTime? = null
): IFitnessProServiceResponse
