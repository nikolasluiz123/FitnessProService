package br.com.fitnesspro.shared.communication.responses

class ImportationServiceResponse<DTO>(
    val executionLogId: String,
    values: List<DTO> = emptyList(),
    code: Int = 0,
    success: Boolean = false,
    error: String? = null,
): ReadServiceResponse<DTO>(values, code, success, error)