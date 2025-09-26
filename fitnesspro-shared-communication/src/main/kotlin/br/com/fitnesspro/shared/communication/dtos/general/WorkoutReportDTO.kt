package br.com.fitnesspro.shared.communication.dtos.general

import br.com.fitnesspro.shared.communication.dtos.general.interfaces.IWorkoutReportDTO
import br.com.fitnesspro.shared.communication.enums.report.EnumReportContext
import java.time.LocalDateTime

data class WorkoutReportDTO(
    override var id: String? = null,
    override var creationDate: LocalDateTime? = null,
    override var updateDate: LocalDateTime? = null,
    override var personId: String?,
    override var workoutId: String?,
    override var reportId: String? = null,
    override var reportContext: EnumReportContext? = null,
    override var active: Boolean = true,
): IWorkoutReportDTO