package br.com.fitnesspro.shared.communication.responses

import br.com.fitnesspro.shared.communication.enums.serviceauth.EnumErrorType
import br.com.fitnesspro.shared.communication.responses.interfaces.ILoggableResponse

class ImportationServiceResponse<DTO>(
    override var executionLogId: String,
    override var executionLogPackageId: String,
    values: List<DTO> = emptyList(),
    code: Int = 0,
    success: Boolean = false,
    error: String? = null,
    errorType: EnumErrorType? = null
): ReadServiceResponse<DTO>(values, code, success, error, errorType), ILoggableResponse