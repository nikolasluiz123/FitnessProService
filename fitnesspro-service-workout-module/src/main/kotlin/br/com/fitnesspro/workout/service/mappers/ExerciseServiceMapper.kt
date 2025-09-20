package br.com.fitnesspro.workout.service.mappers

import br.com.fitnesspro.authentication.repository.auditable.IPersonRepository
import br.com.fitnesspro.models.general.Person
import br.com.fitnesspro.models.workout.Exercise
import br.com.fitnesspro.models.workout.ExerciseExecution
import br.com.fitnesspro.models.workout.ExercisePreDefinition
import br.com.fitnesspro.models.workout.WorkoutGroupPreDefinition
import br.com.fitnesspro.service.communication.dtos.workout.ValidatedExerciseDTO
import br.com.fitnesspro.service.communication.dtos.workout.ValidatedExerciseExecutionDTO
import br.com.fitnesspro.service.communication.dtos.workout.ValidatedExercisePreDefinitionDTO
import br.com.fitnesspro.service.communication.extensions.getOrThrowDefaultException
import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.IExerciseDTO
import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.IExerciseExecutionDTO
import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.IExercisePreDefinitionDTO
import br.com.fitnesspro.workout.repository.auditable.IExerciseExecutionRepository
import br.com.fitnesspro.workout.repository.auditable.IExercisePreDefinitionRepository
import br.com.fitnesspro.workout.repository.auditable.IExerciseRepository
import br.com.fitnesspro.workout.repository.auditable.IWorkoutGroupPreDefinitionRepository
import org.springframework.context.MessageSource
import org.springframework.stereotype.Service

@Service
class ExerciseServiceMapper(
    private val exerciseRepository: IExerciseRepository,
    private val exerciseExecutionRepository: IExerciseExecutionRepository,
    private val exercisePreDefinitionRepository: IExercisePreDefinitionRepository,
    private val workoutGroupPreDefinitionRepository: IWorkoutGroupPreDefinitionRepository,
    private val personRepository: IPersonRepository,
    private val workoutServiceMapper: WorkoutServiceMapper,
    private val messageSource: MessageSource
) {

    fun getExercise(dto: IExerciseDTO): Exercise {
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

    fun getExerciseDTO(model: Exercise): ValidatedExerciseDTO {
        return ValidatedExerciseDTO(
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

    fun getExerciseExecution(dto: IExerciseExecutionDTO): ExerciseExecution {
        val exerciseExecution = dto.id?.let { exerciseExecutionRepository.findById(it) }

        return when {
            dto.id == null -> {
                ExerciseExecution(
                    active = dto.active,
                    duration = dto.duration,
                    repetitions = dto.repetitions,
                    set = dto.set,
                    rest = dto.rest,
                    weight = dto.weight,
                    healthDataCollected = dto.healthDataCollected,
                    executionStartTime = dto.executionStartTime,
                    executionEndTime = dto.executionEndTime,
                    exercise = exerciseRepository.findById(dto.exerciseId!!).getOrThrowDefaultException(messageSource, Exercise::class)
                )
            }

            exerciseExecution?.isPresent == true -> {
                exerciseExecution.get().copy(
                    active = dto.active,
                    duration = dto.duration,
                    repetitions = dto.repetitions,
                    set = dto.set,
                    rest = dto.rest,
                    weight = dto.weight,
                    healthDataCollected = dto.healthDataCollected,
                    executionStartTime = dto.executionStartTime,
                    executionEndTime = dto.executionEndTime,
                    exercise = exerciseRepository.findById(dto.exerciseId!!).getOrThrowDefaultException(messageSource, Exercise::class)
                )
            }

            else -> {
                ExerciseExecution(
                    id = dto.id!!,
                    active = dto.active,
                    duration = dto.duration,
                    repetitions = dto.repetitions,
                    set = dto.set,
                    rest = dto.rest,
                    weight = dto.weight,
                    healthDataCollected = dto.healthDataCollected,
                    executionStartTime = dto.executionStartTime,
                    executionEndTime = dto.executionEndTime,
                    exercise = exerciseRepository.findById(dto.exerciseId!!).getOrThrowDefaultException(messageSource, Exercise::class),
                )
            }
        }
    }

    fun getExerciseExecutionDTO(model: ExerciseExecution): ValidatedExerciseExecutionDTO {
        return ValidatedExerciseExecutionDTO(
            id = model.id,
            creationDate = model.creationDate,
            updateDate = model.updateDate,
            active = model.active,
            duration = model.duration,
            repetitions = model.repetitions,
            set = model.set,
            rest = model.rest,
            weight = model.weight,
            healthDataCollected = model.healthDataCollected,
            executionStartTime = model.executionStartTime,
            executionEndTime = model.executionEndTime,
            exerciseId = model.exercise!!.id
        )
    }


    fun getExercisePreDefinition(dto: IExercisePreDefinitionDTO): ExercisePreDefinition {
        val exercisePreDefinition = dto.id?.let { exercisePreDefinitionRepository.findById(it) }

        return when {
            dto.id == null -> {
                ExercisePreDefinition(
                    active = dto.active,
                    duration = dto.duration,
                    repetitions = dto.repetitions,
                    sets = dto.sets,
                    rest = dto.rest,
                    name = dto.name,
                    exerciseOrder = dto.exerciseOrder,
                    personalTrainerPerson = personRepository.findById(dto.personalTrainerPersonId!!).getOrThrowDefaultException(messageSource, Person::class),
                    workoutGroupPreDefinition = dto.workoutGroupPreDefinitionId?.let {
                        workoutGroupPreDefinitionRepository.findById(it).getOrThrowDefaultException(messageSource, WorkoutGroupPreDefinition::class)
                    }
                )
            }

            exercisePreDefinition?.isPresent == true -> {
                exercisePreDefinition.get().copy(
                    active = dto.active,
                    duration = dto.duration,
                    repetitions = dto.repetitions,
                    sets = dto.sets,
                    rest = dto.rest,
                    name = dto.name,
                    exerciseOrder = dto.exerciseOrder,
                )
            }

            else -> {
                ExercisePreDefinition(
                    id = dto.id!!,
                    active = dto.active,
                    duration = dto.duration,
                    repetitions = dto.repetitions,
                    sets = dto.sets,
                    rest = dto.rest,
                    name = dto.name,
                    exerciseOrder = dto.exerciseOrder,
                    personalTrainerPerson = personRepository.findById(dto.personalTrainerPersonId!!).getOrThrowDefaultException(messageSource, Person::class),
                    workoutGroupPreDefinition = dto.workoutGroupPreDefinitionId?.let {
                        workoutGroupPreDefinitionRepository.findById(it).getOrThrowDefaultException(messageSource, WorkoutGroupPreDefinition::class)
                    }
                )
            }
        }
    }

    fun getExercisePreDefinitionDTO(model: ExercisePreDefinition): ValidatedExercisePreDefinitionDTO {
        return ValidatedExercisePreDefinitionDTO(
            id = model.id,
            creationDate = model.creationDate,
            updateDate = model.updateDate,
            active = model.active,
            name = model.name,
            duration = model.duration,
            repetitions = model.repetitions,
            sets = model.sets,
            rest = model.rest,
            exerciseOrder = model.exerciseOrder,
            workoutGroupPreDefinitionId = model.workoutGroupPreDefinition?.id,
            personalTrainerPersonId = model.personalTrainerPerson!!.id,
        )
    }
}