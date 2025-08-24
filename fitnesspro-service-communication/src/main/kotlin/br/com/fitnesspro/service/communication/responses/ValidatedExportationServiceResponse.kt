package br.com.fitnesspro.service.communication.responses

import br.com.fitnesspro.shared.communication.dtos.common.BaseDTO
import br.com.fitnesspro.shared.communication.enums.serviceauth.EnumErrorType
import br.com.fitnesspro.shared.communication.responses.interfaces.ILoggableResponse
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Resposta padrão para exportação de dados")
class ValidatedExportationServiceResponse(
    @field:Schema(description = "Identificador do log de execução da exportação", required = false)
    override var executionLogId: String,

    @field:Schema(description = "Identificador do log pacote executado da exportação", required = false)
    override var executionLogPackageId: String,

    code: Int = 0,
    success: Boolean = false,
    error: String? = null,
    errorType: EnumErrorType? = null
): ValidatedPersistenceServiceResponse<BaseDTO>(code, success, error, errorType, null), ILoggableResponse