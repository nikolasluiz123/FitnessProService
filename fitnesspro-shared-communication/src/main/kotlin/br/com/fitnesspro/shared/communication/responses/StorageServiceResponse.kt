package br.com.fitnesspro.shared.communication.responses

import br.com.fitnesspro.shared.communication.enums.serviceauth.EnumErrorType
import br.com.fitnesspro.shared.communication.responses.interfaces.ILoggableResponse

class StorageServiceResponse(
    override var executionLogId: String,
    override var executionLogPackageId: String,
    override var code: Int = 0,
    override var success: Boolean = false,
    override var error: String? = null,
    override var errorType: EnumErrorType? = null
): ILoggableResponse