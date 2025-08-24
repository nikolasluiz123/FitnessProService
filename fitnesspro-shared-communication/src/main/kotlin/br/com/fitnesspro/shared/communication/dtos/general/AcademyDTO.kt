package br.com.fitnesspro.shared.communication.dtos.general

import br.com.fitnesspro.shared.communication.dtos.general.interfaces.IAcademyDTO
import java.time.LocalDateTime

data class AcademyDTO(
    override var id: String? = null,
    override var creationDate: LocalDateTime? = null,
    override var updateDate: LocalDateTime? = null,
    override var name: String? = null,
    override var address: String? = null,
    override var phone: String? = null,
    override var active: Boolean = true
): IAcademyDTO