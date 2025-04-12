package br.com.fitnesspro.shared.communication.responses

import br.com.fitnesspro.shared.communication.enums.serviceauth.EnumErrorType

class ExportationServiceResponse(
    var executionLogId: String,
    var executionLogPackageId: String,
    code: Int = 0,
    success: Boolean = false,
    error: String? = null,
    id: String? = null,
    errorType: EnumErrorType? = null
): PersistenceServiceResponse(code, success, error, errorType, id)