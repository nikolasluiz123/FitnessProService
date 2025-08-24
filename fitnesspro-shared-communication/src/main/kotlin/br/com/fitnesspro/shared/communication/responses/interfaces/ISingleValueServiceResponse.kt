package br.com.fitnesspro.shared.communication.responses.interfaces

interface ISingleValueServiceResponse<T> : IFitnessProServiceResponse {
    var value: T?
}