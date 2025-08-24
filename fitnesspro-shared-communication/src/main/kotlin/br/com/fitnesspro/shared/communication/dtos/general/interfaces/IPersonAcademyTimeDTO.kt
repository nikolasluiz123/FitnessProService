package br.com.fitnesspro.shared.communication.dtos.general.interfaces

import br.com.fitnesspro.shared.communication.dtos.common.AuditableDTO
import java.time.DayOfWeek
import java.time.LocalTime

interface IPersonAcademyTimeDTO: AuditableDTO {
    val personId: String?
    val academyId: String?
    val timeStart: LocalTime?
    val timeEnd: LocalTime?
    val dayOfWeek: DayOfWeek?
    val active: Boolean
}