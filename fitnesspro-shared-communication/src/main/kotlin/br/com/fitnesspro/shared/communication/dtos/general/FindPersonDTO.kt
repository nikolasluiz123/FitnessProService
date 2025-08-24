package br.com.fitnesspro.shared.communication.dtos.general

import br.com.fitnesspro.shared.communication.dtos.general.interfaces.IFindPersonDTO

data class FindPersonDTO(
    override var email: String? = null,
    override var password: String? = null,
): IFindPersonDTO