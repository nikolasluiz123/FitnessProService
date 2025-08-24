package br.com.fitnesspro.shared.communication.dtos.workout

import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.IExerciseExecutionDTO
import java.time.LocalDateTime

data class ExerciseExecutionDTO(
    override var id: String? = null,
    override var creationDate: LocalDateTime? = null,
    override var updateDate: LocalDateTime? = null,
    override var active: Boolean = true,
    override var duration: Long? = null,
    override var repetitions: Int? = null,
    override var set: Int? = null,
    override var rest: Long? = null,
    override var weight: Double? = null,
    override var date: LocalDateTime? = null,
    override var exerciseId: String? = null,
) : IExerciseExecutionDTO