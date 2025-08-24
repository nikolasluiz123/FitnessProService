package br.com.fitnesspro.shared.communication.responses

import br.com.fitnesspro.shared.communication.enums.serviceauth.EnumErrorType
import br.com.fitnesspro.shared.communication.responses.interfaces.IReadServiceResponse

open class ReadServiceResponse<DTO>(
    override var values: List<DTO> = emptyList(),
    override var code: Int = 0,
    override var success: Boolean = false,
    override var error: String? = null,
    override val errorType: EnumErrorType? = null,
) : IReadServiceResponse<DTO>