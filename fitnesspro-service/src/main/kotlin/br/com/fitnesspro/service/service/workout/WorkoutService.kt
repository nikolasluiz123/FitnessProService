package br.com.fitnesspro.service.service.workout

import br.com.fitnesspro.service.models.workout.Workout
import br.com.fitnesspro.service.models.workout.WorkoutGroup
import br.com.fitnesspro.service.repository.general.person.IPersonRepository
import br.com.fitnesspro.service.repository.general.user.IUserRepository
import br.com.fitnesspro.service.repository.workout.IWorkoutGroupRepository
import br.com.fitnesspro.service.repository.workout.IWorkoutRepository
import br.com.fitnesspro.shared.communication.dtos.workout.WorkoutDTO
import br.com.fitnesspro.shared.communication.dtos.workout.WorkoutGroupDTO
import org.springframework.stereotype.Service

@Service
class WorkoutService(
    private val workoutRepository: IWorkoutRepository,
    private val workoutGroupRepository: IWorkoutGroupRepository,
    private val personRepository: IPersonRepository,
    private val userRepository: IUserRepository
) {

    fun saveWorkout(workout: WorkoutDTO) {
        workoutRepository.save(workout.toWorkout())
    }

    fun saveWorkoutBatch(workouts: List<WorkoutDTO>) {
        workoutRepository.saveAll(workouts.map { it.toWorkout() })
    }

    fun saveWorkoutGroup(workoutGroup: WorkoutGroupDTO) {
        workoutGroupRepository.save(workoutGroup.toWorkoutGroup())
    }

    fun saveWorkoutGroupBatch(workoutGroups: List<WorkoutGroupDTO>) {
        workoutGroupRepository.saveAll(workoutGroups.map { it.toWorkoutGroup() })
    }

    private fun WorkoutDTO.toWorkout(): Workout {
        val workout = id?.let { workoutRepository.findById(it) }

        return when {
            id == null -> {
                Workout(
                    active = active,
                    academyMemberPerson = personRepository.findById(academyMemberPersonId!!).get(),
                    professionalPerson = personRepository.findById(professionalPersonId!!).get(),
                    dateStart = dateStart,
                    dateEnd = dateEnd,
                    creationUser = userRepository.findById(creationUserId!!).get(),
                    updateUser = userRepository.findById(updateUserId!!).get()
                )
            }

            workout?.isPresent ?: false -> {
                workout!!.get().copy(
                    active = active,
                    academyMemberPerson = personRepository.findById(academyMemberPersonId!!).get(),
                    professionalPerson = personRepository.findById(professionalPersonId!!).get(),
                    dateStart = dateStart,
                    dateEnd = dateEnd,
                    updateUser = userRepository.findById(updateUserId!!).get()
                )
            }

            else -> {
                Workout(
                    id = id!!,
                    active = active,
                    academyMemberPerson = personRepository.findById(academyMemberPersonId!!).get(),
                    professionalPerson = personRepository.findById(professionalPersonId!!).get(),
                    dateStart = dateStart,
                    dateEnd = dateEnd,
                    creationUser = userRepository.findById(creationUserId!!).get(),
                    updateUser = userRepository.findById(updateUserId!!).get()
                )
            }
        }
    }

    private fun WorkoutGroupDTO.toWorkoutGroup(): WorkoutGroup {
        val workoutGroup = id?.let { workoutGroupRepository.findById(it) }

        return when {
            id == null -> {
                WorkoutGroup(
                    active = active,
                    name = name!!,
                    workout = workoutRepository.findById(workoutId!!).get(),
                    dayWeek = dayWeek!!,
                    creationUser = userRepository.findById(creationUserId!!).get(),
                    updateUser = userRepository.findById(updateUserId!!).get()
                )
            }

            workoutGroup?.isPresent ?: false -> {
                workoutGroup!!.get().copy(
                    active = active,
                    name = name!!,
                    workout = workoutRepository.findById(workoutId!!).get(),
                    dayWeek = dayWeek!!,
                    updateUser = userRepository.findById(updateUserId!!).get()
                )
            }

            else -> {
                WorkoutGroup(
                    id = id!!,
                    active = active,
                    name = name!!,
                    workout = workoutRepository.findById(workoutId!!).get(),
                    dayWeek = dayWeek!!,
                    creationUser = userRepository.findById(creationUserId!!).get(),
                    updateUser = userRepository.findById(updateUserId!!).get()
                )
            }
        }
    }
}