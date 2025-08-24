package br.com.fitnesspro.shared.communication.dtos.workout

import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.IWorkoutGroupPreDefinitionDTO
import java.time.LocalDateTime

data class WorkoutGroupPreDefinitionDTO(
    override var id: String? = null,
    override var creationDate: LocalDateTime? = null,
    override var updateDate: LocalDateTime? = null,
    override var active: Boolean = true,
    override var name: String? = null,
    override var personalTrainerPersonId: String? = null
) : IWorkoutGroupPreDefinitionDTO
