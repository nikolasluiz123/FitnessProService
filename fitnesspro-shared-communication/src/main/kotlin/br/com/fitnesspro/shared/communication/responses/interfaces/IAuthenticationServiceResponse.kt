package br.com.fitnesspro.shared.communication.responses.interfaces

import br.com.fitnesspro.shared.communication.dtos.serviceauth.interfaces.IServiceTokenDTO

interface IAuthenticationServiceResponse: IFitnessProServiceResponse {
    val tokens: List<IServiceTokenDTO>
}