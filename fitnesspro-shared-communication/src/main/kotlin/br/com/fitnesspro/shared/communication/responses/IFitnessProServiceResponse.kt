package br.com.fitnesspro.shared.communication.responses

import java.io.Serializable

interface IFitnessProServiceResponse : Serializable {
    var code: Int
    var success: Boolean
    var error: String?
}