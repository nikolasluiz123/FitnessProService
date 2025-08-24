package br.com.fitnesspro.shared.communication.responses

import br.com.fitnesspro.shared.communication.dtos.common.BaseDTO
import br.com.fitnesspro.shared.communication.enums.serviceauth.EnumErrorType
import br.com.fitnesspro.shared.communication.responses.interfaces.ILoggableResponse

class ExportationServiceResponse(
    override var executionLogId: String,
    override var executionLogPackageId: String,
    code: Int = 0,
    success: Boolean = false,
    error: String? = null,
    errorType: EnumErrorType? = null
): PersistenceServiceResponse<BaseDTO>(code, success, error, errorType, null), ILoggableResponse