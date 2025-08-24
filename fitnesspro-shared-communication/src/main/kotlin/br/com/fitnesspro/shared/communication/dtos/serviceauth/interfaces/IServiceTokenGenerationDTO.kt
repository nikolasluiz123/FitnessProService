package br.com.fitnesspro.shared.communication.dtos.serviceauth.interfaces

import br.com.fitnesspro.shared.communication.enums.serviceauth.EnumTokenType

interface IServiceTokenGenerationDTO {
    var type: EnumTokenType?
    var userId: String?
    var deviceId: String?
    var applicationId: String?
}