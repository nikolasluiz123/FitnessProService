package br.com.fitnesspro.shared.communication.dtos.general

import br.com.fitnesspro.shared.communication.dtos.general.interfaces.IPersonAcademyTimeDTO
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime

data class PersonAcademyTimeDTO(
    override var id: String? = null,
    override var creationDate: LocalDateTime? = null,
    override var updateDate: LocalDateTime? = null,
    override val personId: String? = null,
    override val academyId: String? = null,
    override val timeStart: LocalTime? = null,
    override val timeEnd: LocalTime? = null,
    override val dayOfWeek: DayOfWeek? = null,
    override val active: Boolean = true
): IPersonAcademyTimeDTO