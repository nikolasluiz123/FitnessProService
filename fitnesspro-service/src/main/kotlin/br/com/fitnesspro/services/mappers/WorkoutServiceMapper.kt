package br.com.fitnesspro.services.mappers

import br.com.fitnesspro.models.workout.Workout
import br.com.fitnesspro.models.workout.WorkoutGroup
import br.com.fitnesspro.repository.general.person.IPersonRepository
import br.com.fitnesspro.repository.workout.IWorkoutGroupRepository
import br.com.fitnesspro.repository.workout.IWorkoutRepository
import br.com.fitnesspro.shared.communication.dtos.workout.WorkoutDTO
import br.com.fitnesspro.shared.communication.dtos.workout.WorkoutGroupDTO
import org.springframework.stereotype.Service

@Service
class WorkoutServiceMapper(
    private val workoutRepository: IWorkoutRepository,
    private val workoutGroupRepository: IWorkoutGroupRepository,
    private val personRepository: IPersonRepository
) {

    fun getWorkout(dto: WorkoutDTO): Workout {
        val workout = dto.id?.let { workoutRepository.findById(it) }

        return when {
            dto.id == null -> {
                Workout(
                    active = dto.active,
                    academyMemberPerson = personRepository.findById(dto.academyMemberPersonId!!).get(),
                    professionalPerson = personRepository.findById(dto.professionalPersonId!!).get(),
                    dateStart = dto.dateStart,
                    dateEnd = dto.dateEnd,
                )
            }

            workout?.isPresent == true -> {
                workout.get().copy(
                    active = dto.active,
                    academyMemberPerson = personRepository.findById(dto.academyMemberPersonId!!).get(),
                    professionalPerson = personRepository.findById(dto.professionalPersonId!!).get(),
                    dateStart = dto.dateStart,
                    dateEnd = dto.dateEnd,
                )
            }

            else -> {
                Workout(
                    id = dto.id!!,
                    active = dto.active,
                    academyMemberPerson = personRepository.findById(dto.academyMemberPersonId!!).get(),
                    professionalPerson = personRepository.findById(dto.professionalPersonId!!).get(),
                    dateStart = dto.dateStart,
                    dateEnd = dto.dateEnd,
                )
            }
        }
    }

    fun getWorkoutGroup(dto: WorkoutGroupDTO): WorkoutGroup {
        val workoutGroup = dto.id?.let { workoutGroupRepository.findById(it) }

        return when {
            dto.id == null -> {
                WorkoutGroup(
                    active = dto.active,
                    name = dto.name!!,
                    workout = workoutRepository.findById(dto.workoutId!!).get(),
                    dayWeek = dto.dayWeek!!,
                )
            }

            workoutGroup?.isPresent == true -> {
                workoutGroup.get().copy(
                    active = dto.active,
                    name = dto.name!!,
                    workout = workoutRepository.findById(dto.workoutId!!).get(),
                    dayWeek = dto.dayWeek!!,
                )
            }

            else -> {
                WorkoutGroup(
                    id = dto.id!!,
                    active = dto.active,
                    name = dto.name!!,
                    workout = workoutRepository.findById(dto.workoutId!!).get(),
                    dayWeek = dto.dayWeek!!,
                )
            }
        }
    }
}