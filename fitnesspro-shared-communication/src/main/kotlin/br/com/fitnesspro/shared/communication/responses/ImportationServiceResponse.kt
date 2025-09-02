package br.com.fitnesspro.shared.communication.responses

import br.com.fitnesspro.shared.communication.enums.serviceauth.EnumErrorType
import br.com.fitnesspro.shared.communication.responses.interfaces.ILoggableResponse

class ImportationServiceResponse<DTO>(
    override var executionLogId: String,
    override var executionLogPackageId: String,
    value: DTO? = null,
    code: Int = 0,
    success: Boolean = false,
    error: String? = null,
    errorType: EnumErrorType? = null
): SingleValueServiceResponse<DTO>(value, code, success, error, errorType), ILoggableResponse