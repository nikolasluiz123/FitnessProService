package br.com.fitnesspro.services.workout

import br.com.fitnesspro.repository.workout.IWorkoutGroupRepository
import br.com.fitnesspro.repository.workout.IWorkoutRepository
import br.com.fitnesspro.services.mappers.WorkoutServiceMapper
import br.com.fitnesspro.shared.communication.dtos.workout.WorkoutDTO
import br.com.fitnesspro.shared.communication.dtos.workout.WorkoutGroupDTO
import org.springframework.stereotype.Service

@Service
class WorkoutService(
    private val workoutRepository: IWorkoutRepository,
    private val workoutGroupRepository: IWorkoutGroupRepository,
    private val workoutServiceMapper: WorkoutServiceMapper
) {

    fun saveWorkout(workoutDTO: WorkoutDTO) {
        workoutRepository.save(workoutServiceMapper.getWorkout(workoutDTO))
    }

    fun saveWorkoutBatch(workouts: List<WorkoutDTO>) {
        workoutRepository.saveAll(workouts.map(workoutServiceMapper::getWorkout))
    }

    fun saveWorkoutGroup(workoutGroupDTO: WorkoutGroupDTO) {
        workoutGroupRepository.save(workoutServiceMapper.getWorkoutGroup(workoutGroupDTO))
    }

    fun saveWorkoutGroupBatch(workoutGroups: List<WorkoutGroupDTO>) {
        workoutGroupRepository.saveAll(workoutGroups.map(workoutServiceMapper::getWorkoutGroup))
    }
}