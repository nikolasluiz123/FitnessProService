package br.com.fitnesspro.shared.communication.dtos.workout

import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.IWorkoutDTO
import java.time.LocalDate
import java.time.LocalDateTime

data class WorkoutDTO(
    override var id: String? = null,
    override var creationDate: LocalDateTime? = null,
    override var updateDate: LocalDateTime? = null,
    override var active: Boolean = true,
    override var academyMemberPersonId: String? = null,
    override var professionalPersonId: String? = null,
    override var dateStart: LocalDate? = null,
    override var dateEnd: LocalDate? = null,
) : IWorkoutDTO