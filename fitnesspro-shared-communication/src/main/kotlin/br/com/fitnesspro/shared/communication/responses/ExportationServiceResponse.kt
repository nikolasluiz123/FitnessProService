package br.com.fitnesspro.shared.communication.responses

class ExportationServiceResponse(
    var executionLogId: String,
    code: Int = 0,
    success: Boolean = false,
    error: String? = null,
    id: String? = null,
): PersistenceServiceResponse(code, success, error, id)