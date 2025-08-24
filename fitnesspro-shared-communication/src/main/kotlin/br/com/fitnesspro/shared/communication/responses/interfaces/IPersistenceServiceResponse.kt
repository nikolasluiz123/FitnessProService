package br.com.fitnesspro.shared.communication.responses.interfaces

import br.com.fitnesspro.shared.communication.dtos.common.BaseDTO

interface IPersistenceServiceResponse<T: BaseDTO>: IFitnessProServiceResponse {
    val savedDTO: T?
}
