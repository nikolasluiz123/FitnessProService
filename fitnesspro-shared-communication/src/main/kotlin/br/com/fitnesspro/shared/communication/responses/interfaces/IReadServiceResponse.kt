package br.com.fitnesspro.shared.communication.responses.interfaces

interface IReadServiceResponse<DTO>: IFitnessProServiceResponse {
    var values: List<DTO>
}