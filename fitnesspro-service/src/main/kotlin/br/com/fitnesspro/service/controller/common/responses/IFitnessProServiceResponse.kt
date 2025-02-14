package br.com.fitnesspro.service.controller.common.responses

import java.io.Serializable

interface IFitnessProServiceResponse : Serializable {
    var code: Int
    var success: Boolean
    var error: String?
}