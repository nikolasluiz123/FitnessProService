package br.com.fitnesspro.shared.communication.responses.interfaces

import br.com.fitnesspro.shared.communication.enums.serviceauth.EnumErrorType
import java.io.Serializable

interface IFitnessProServiceResponse : Serializable {
    var code: Int
    var success: Boolean
    var error: String?
    val errorType: EnumErrorType?

    fun isSuccessWithoutConnection(): Boolean {
        return success || errorType == EnumErrorType.NETWORK
    }
}