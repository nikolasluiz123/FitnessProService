package br.com.fitnesspro.shared.communication.dtos.serviceauth.interfaces

import br.com.fitnesspro.shared.communication.dtos.common.BaseDTO

interface IApplicationDTO : BaseDTO {
    var name: String?
    var active: Boolean
}