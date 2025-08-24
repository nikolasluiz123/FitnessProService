package br.com.fitnesspro.shared.communication.responses

import br.com.fitnesspro.shared.communication.dtos.common.BaseDTO
import br.com.fitnesspro.shared.communication.enums.serviceauth.EnumErrorType
import br.com.fitnesspro.shared.communication.responses.interfaces.IPersistenceServiceResponse

open class PersistenceServiceResponse<T: BaseDTO>(
    override var code: Int = 0,
    override var success: Boolean = false,
    override var error: String? = null,
    override val errorType: EnumErrorType? = null,
    override val savedDTO: T? = null
): IPersistenceServiceResponse<T>
