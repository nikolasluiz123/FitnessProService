package br.com.fitnesspro.shared.communication.dtos.scheduler

import br.com.fitnesspro.shared.communication.dtos.scheduler.interfaces.IRecurrentSchedulerDTO
import br.com.fitnesspro.shared.communication.dtos.workout.WorkoutDTO
import br.com.fitnesspro.shared.communication.dtos.workout.WorkoutGroupDTO

data class RecurrentSchedulerDTO(
    override val schedules: List<SchedulerDTO> = emptyList(),
    override val workoutDTO: WorkoutDTO? = null,
    override val workoutGroups: List<WorkoutGroupDTO> = emptyList()
) : IRecurrentSchedulerDTO