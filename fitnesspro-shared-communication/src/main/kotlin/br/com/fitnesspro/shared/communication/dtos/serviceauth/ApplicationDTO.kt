package br.com.fitnesspro.shared.communication.dtos.serviceauth

import br.com.fitnesspro.shared.communication.dtos.serviceauth.interfaces.IApplicationDTO

data class ApplicationDTO(
    override var id: String? = null,
    override var name: String? = null,
    override var active: Boolean = true,
) : IApplicationDTO