package br.com.fitnesspro.shared.communication.dtos.general.interfaces

import br.com.fitnesspro.shared.communication.dtos.common.AuditableDTO

interface IAcademyDTO: AuditableDTO {
    var name: String?
    var address: String?
    var phone: String?
    var active: Boolean
}