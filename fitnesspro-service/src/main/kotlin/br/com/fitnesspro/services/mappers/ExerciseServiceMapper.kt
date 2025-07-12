package br.com.fitnesspro.services.mappers

import br.com.fitnesspro.models.workout.Exercise
import br.com.fitnesspro.repository.auditable.workout.IExerciseRepository
import br.com.fitnesspro.shared.communication.dtos.workout.ExerciseDTO
import org.springframework.stereotype.Service

@Service
class ExerciseServiceMapper(
    private val exerciseRepository: IExerciseRepository,
    private val workoutServiceMapper: WorkoutServiceMapper
) {

    fun getExercise(dto: ExerciseDTO): Exercise {
        val exercise = dto.id?.let { exerciseRepository.findById(it) }

        return when {
            dto.id == null -> {
                Exercise(
                    active = dto.active,
                    name = dto.name!!,
                    duration = dto.duration,
                    repetitions = dto.repetitions,
                    sets = dto.sets,
                    rest = dto.rest,
                    observation = dto.observation,
                    exerciseOrder = dto.exerciseOrder!!,
                    workoutGroup = workoutServiceMapper.getWorkoutGroup(dto.workoutGroupDTO!!)
                )
            }

            exercise?.isPresent == true -> {
                exercise.get().copy(
                    active = dto.active,
                    name = dto.name!!,
                    duration = dto.duration,
                    repetitions = dto.repetitions,
                    sets = dto.sets,
                    rest = dto.rest,
                    observation = dto.observation,
                    exerciseOrder = dto.exerciseOrder!!,
                    workoutGroup = dto.workoutGroupDTO?.let(workoutServiceMapper::getWorkoutGroup) ?: exercise.get().workoutGroup!!,
                )
            }

            else -> {
                Exercise(
                    id = dto.id!!,
                    active = dto.active,
                    name = dto.name!!,
                    duration = dto.duration,
                    repetitions = dto.repetitions,
                    sets = dto.sets,
                    rest = dto.rest,
                    observation = dto.observation,
                    exerciseOrder = dto.exerciseOrder!!,
                    workoutGroup = workoutServiceMapper.getWorkoutGroup(dto.workoutGroupDTO!!)
                )
            }
        }
    }

    fun getExerciseDTO(model: Exercise): ExerciseDTO {
        return ExerciseDTO(
            id = model.id,
            creationDate = model.creationDate,
            updateDate = model.updateDate,
            active = model.active,
            name = model.name,
            duration = model.duration,
            repetitions = model.repetitions,
            sets = model.sets,
            rest = model.rest,
            observation = model.observation,
            exerciseOrder = model.exerciseOrder,
            workoutGroupDTO = workoutServiceMapper.getWorkoutGroupDTO(model.workoutGroup!!)
        )
    }
}