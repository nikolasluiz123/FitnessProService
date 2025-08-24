package br.com.fitnesspro.shared.communication.dtos.serviceauth.interfaces

import br.com.fitnesspro.shared.communication.dtos.common.BaseDTO

interface IApplicationDTO : BaseDTO {
    val name: String?
    var active: Boolean
}