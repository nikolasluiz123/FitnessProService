package br.com.fitnesspro.shared.communication.dtos.general

import br.com.fitnesspro.shared.communication.dtos.general.interfaces.IPersonDTO
import br.com.fitnesspro.shared.communication.dtos.general.interfaces.IUserDTO
import java.time.LocalDate
import java.time.LocalDateTime

data class PersonDTO(
    override var id: String? = null,
    override var creationDate: LocalDateTime? = null,
    override var updateDate: LocalDateTime? = null,
    override var name: String? = null,
    override var birthDate: LocalDate? = null,
    override var phone: String? = null,
    override var user: IUserDTO? = null,
    override var active: Boolean = true,
    override var createDefaultSchedulerConfig: Boolean = false
): IPersonDTO