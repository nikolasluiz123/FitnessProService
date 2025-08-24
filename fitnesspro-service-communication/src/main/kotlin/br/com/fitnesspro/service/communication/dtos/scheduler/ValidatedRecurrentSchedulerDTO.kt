package br.com.fitnesspro.service.communication.dtos.scheduler

import br.com.fitnesspro.service.communication.dtos.workout.ValidatedWorkoutDTO
import br.com.fitnesspro.service.communication.dtos.workout.ValidatedWorkoutGroupDTO
import br.com.fitnesspro.shared.communication.dtos.scheduler.interfaces.IRecurrentSchedulerDTO
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull

@Schema(description = "Classe DTO usada para criação de agendamento recorrente")
data class ValidatedRecurrentSchedulerDTO(
    @field:Schema(description = "Lista de agendamentos", required = true)
    @field:NotNull(message = "recurrentSchedulerDTO.schedules.notNull")
    @field:NotEmpty(message = "recurrentSchedulerDTO.schedules.notNull")
    override val schedules: List<ValidatedSchedulerDTO> = emptyList(),

    @field:Schema(description = "Treino gerado com os dados do agendamento recorrente")
    @field:NotNull(message = "recurrentSchedulerDTO.workout.notNull")
    override val workoutDTO: ValidatedWorkoutDTO? = null,

    @field:Schema(description = "Lista de grupos musculares do treino gerados com os dados do agendamento recorrente")
    @field:NotNull(message = "recurrentSchedulerDTO.workoutGroups.notNull")
    @field:NotEmpty(message = "recurrentSchedulerDTO.workoutGroups.notNull")
    override val workoutGroups: List<ValidatedWorkoutGroupDTO> = emptyList()
) : IRecurrentSchedulerDTO