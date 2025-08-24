package br.com.fitnesspro.shared.communication.responses.interfaces

interface ILoggableResponse: IFitnessProServiceResponse {
    var executionLogId: String
    var executionLogPackageId: String
}