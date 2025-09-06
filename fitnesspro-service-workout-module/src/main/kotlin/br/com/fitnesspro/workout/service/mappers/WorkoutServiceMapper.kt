package br.com.fitnesspro.workout.service.mappers

import br.com.fitnesspro.authentication.repository.auditable.IPersonRepository
import br.com.fitnesspro.models.general.Person
import br.com.fitnesspro.models.workout.ExercisePreDefinition
import br.com.fitnesspro.models.workout.Workout
import br.com.fitnesspro.models.workout.WorkoutGroup
import br.com.fitnesspro.models.workout.WorkoutGroupPreDefinition
import br.com.fitnesspro.service.communication.dtos.workout.ValidatedWorkoutDTO
import br.com.fitnesspro.service.communication.dtos.workout.ValidatedWorkoutGroupDTO
import br.com.fitnesspro.service.communication.dtos.workout.ValidatedWorkoutGroupPreDefinitionDTO
import br.com.fitnesspro.service.communication.extensions.getOrThrowDefaultException
import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.IWorkoutDTO
import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.IWorkoutGroupDTO
import br.com.fitnesspro.shared.communication.dtos.workout.interfaces.IWorkoutGroupPreDefinitionDTO
import br.com.fitnesspro.workout.repository.auditable.IWorkoutGroupPreDefinitionRepository
import br.com.fitnesspro.workout.repository.auditable.IWorkoutGroupRepository
import br.com.fitnesspro.workout.repository.auditable.IWorkoutRepository
import org.springframework.context.MessageSource
import org.springframework.stereotype.Service

@Service
class WorkoutServiceMapper(
    private val workoutRepository: IWorkoutRepository,
    private val workoutGroupRepository: IWorkoutGroupRepository,
    private val workoutGroupPreDefinitionRepository: IWorkoutGroupPreDefinitionRepository,
    private val personRepository: IPersonRepository,
    private val messageSource: MessageSource
) {

    fun getWorkout(dto: IWorkoutDTO): Workout {
        val workout = dto.id?.let { workoutRepository.findById(it) }

        return when {
            dto.id == null -> {
                Workout(
                    active = dto.active,
                    academyMemberPerson = personRepository.findById(dto.academyMemberPersonId!!).getOrThrowDefaultException(messageSource, Person::class),
                    professionalPerson = personRepository.findById(dto.professionalPersonId!!).getOrThrowDefaultException(messageSource, Person::class),
                    dateStart = dto.dateStart,
                    dateEnd = dto.dateEnd,
                )
            }

            workout?.isPresent == true -> {
                workout.get().copy(
                    active = dto.active,
                    academyMemberPerson = personRepository.findById(dto.academyMemberPersonId!!).getOrThrowDefaultException(messageSource, Person::class),
                    professionalPerson = personRepository.findById(dto.professionalPersonId!!).getOrThrowDefaultException(messageSource, Person::class),
                    dateStart = dto.dateStart,
                    dateEnd = dto.dateEnd,
                )
            }

            else -> {
                Workout(
                    id = dto.id!!,
                    active = dto.active,
                    academyMemberPerson = personRepository.findById(dto.academyMemberPersonId!!).getOrThrowDefaultException(messageSource, Person::class),
                    professionalPerson = personRepository.findById(dto.professionalPersonId!!).getOrThrowDefaultException(messageSource, Person::class),
                    dateStart = dto.dateStart,
                    dateEnd = dto.dateEnd,
                )
            }
        }
    }

    fun getWorkoutGroup(dto: IWorkoutGroupDTO): WorkoutGroup {
        val workoutGroup = dto.id?.let { workoutGroupRepository.findById(it) }

        return when {
            dto.id == null -> {
                WorkoutGroup(
                    active = dto.active,
                    name = dto.name,
                    workout = workoutRepository.findById(dto.workoutId!!).getOrThrowDefaultException(messageSource, Workout::class),
                    dayWeek = dto.dayWeek!!,
                    groupOrder = dto.groupOrder,
                )
            }

            workoutGroup?.isPresent == true -> {
                workoutGroup.get().copy(
                    active = dto.active,
                    name = dto.name,
                    workout = workoutRepository.findById(dto.workoutId!!).getOrThrowDefaultException(messageSource, Workout::class),
                    dayWeek = dto.dayWeek!!,
                    groupOrder = dto.groupOrder,
                )
            }

            else -> {
                WorkoutGroup(
                    id = dto.id!!,
                    active = dto.active,
                    name = dto.name,
                    workout = workoutRepository.findById(dto.workoutId!!).getOrThrowDefaultException(messageSource, Workout::class),
                    dayWeek = dto.dayWeek!!,
                    groupOrder = dto.groupOrder,
                )
            }
        }
    }

    fun getWorkoutGroupDTO(workoutGroup: WorkoutGroup): ValidatedWorkoutGroupDTO {
        return ValidatedWorkoutGroupDTO(
            id = workoutGroup.id,
            creationDate = workoutGroup.creationDate,
            updateDate = workoutGroup.updateDate,
            active = workoutGroup.active,
            name = workoutGroup.name,
            workoutId = workoutGroup.workout?.id,
            dayWeek = workoutGroup.dayWeek,
            groupOrder = workoutGroup.groupOrder,
        )
    }

    fun getWorkoutDTO(workout: Workout): ValidatedWorkoutDTO {
        return ValidatedWorkoutDTO(
            id = workout.id,
            creationDate = workout.creationDate,
            updateDate = workout.updateDate,
            active = workout.active,
            academyMemberPersonId = workout.academyMemberPerson?.id,
            professionalPersonId = workout.professionalPerson?.id,
            dateStart = workout.dateStart,
            dateEnd = workout.dateEnd,
        )
    }

    fun getWorkoutGroupPreDefinition(dto: IWorkoutGroupPreDefinitionDTO): WorkoutGroupPreDefinition {
        val workoutGroupPreDefinition = dto.id?.let { workoutGroupPreDefinitionRepository.findById(it) }

        return when {
            dto.id == null -> {
                WorkoutGroupPreDefinition(
                    active = dto.active,
                    name = dto.name,
                    personalTrainerPerson = personRepository.findById(dto.personalTrainerPersonId!!).getOrThrowDefaultException(messageSource, Person::class)
                )
            }

            workoutGroupPreDefinition?.isPresent == true -> {
                workoutGroupPreDefinition.get().copy(
                    active = dto.active,
                    name = dto.name,
                )
            }

            else -> {
                WorkoutGroupPreDefinition(
                    id = dto.id!!,
                    active = dto.active,
                    name = dto.name,
                    personalTrainerPerson = personRepository.findById(dto.personalTrainerPersonId!!).getOrThrowDefaultException(messageSource, Person::class)
                )
            }
        }
    }

    fun getWorkoutGroupPreDefinitionDTO(model: WorkoutGroupPreDefinition): ValidatedWorkoutGroupPreDefinitionDTO {
        return ValidatedWorkoutGroupPreDefinitionDTO(
            id = model.id,
            creationDate = model.creationDate,
            updateDate = model.updateDate,
            active = model.active,
            name = model.name,
            personalTrainerPersonId = model.personalTrainerPerson?.id,
        )
    }
}