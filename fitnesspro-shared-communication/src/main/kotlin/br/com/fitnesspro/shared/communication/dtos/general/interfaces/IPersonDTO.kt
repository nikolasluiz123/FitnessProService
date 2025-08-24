package br.com.fitnesspro.shared.communication.dtos.general.interfaces

import br.com.fitnesspro.shared.communication.dtos.common.AuditableDTO
import java.time.LocalDate

interface IPersonDTO: AuditableDTO {
    var name: String?
    var birthDate: LocalDate?
    var phone: String?
    var user: IUserDTO?
    var active: Boolean
    var createDefaultSchedulerConfig: Boolean
}