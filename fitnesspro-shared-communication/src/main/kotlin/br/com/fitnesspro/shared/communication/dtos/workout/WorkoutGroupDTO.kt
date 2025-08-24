package br.com.fitnesspro.shared.communication.dtos.workout

import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.IWorkoutGroupDTO
import java.time.DayOfWeek
import java.time.LocalDateTime

data class WorkoutGroupDTO(
    override var id: String? = null,
    override var creationDate: LocalDateTime? = null,
    override var updateDate: LocalDateTime? = null,
    override var active: Boolean = true,
    override var name: String? = null,
    override var workoutId: String? = null,
    override var dayWeek: DayOfWeek? = null,
    override var groupOrder: Int = 1,
) : IWorkoutGroupDTO