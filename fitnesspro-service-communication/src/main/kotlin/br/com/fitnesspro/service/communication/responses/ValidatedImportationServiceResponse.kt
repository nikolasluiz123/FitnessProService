package br.com.fitnesspro.service.communication.responses

import br.com.fitnesspro.shared.communication.enums.serviceauth.EnumErrorType
import br.com.fitnesspro.shared.communication.responses.SingleValueServiceResponse
import br.com.fitnesspro.shared.communication.responses.interfaces.ILoggableResponse
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Resposta padrão do serviço para importações")
class ValidatedImportationServiceResponse<DTO>(
    @field:Schema(description = "Identificador do log de execução da importação", required = false)
    override var executionLogId: String,

    @field:Schema(description = "Identificador do log pacote executado da importação", required = false)
    override var executionLogPackageId: String,

    value: DTO? = null,
    code: Int = 0,
    success: Boolean = false,
    error: String? = null,
    errorType: EnumErrorType? = null
): SingleValueServiceResponse<DTO>(value, code, success, error, errorType), ILoggableResponse