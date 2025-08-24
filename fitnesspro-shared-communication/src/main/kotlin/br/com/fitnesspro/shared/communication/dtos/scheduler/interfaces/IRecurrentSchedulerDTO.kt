package br.com.fitnesspro.shared.communication.dtos.scheduler.interfaces

import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.IWorkoutDTO
import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.IWorkoutGroupDTO

interface IRecurrentSchedulerDTO {
    val schedules: List<ISchedulerDTO>
    val workoutDTO: IWorkoutDTO?
    val workoutGroups: List<IWorkoutGroupDTO>
}