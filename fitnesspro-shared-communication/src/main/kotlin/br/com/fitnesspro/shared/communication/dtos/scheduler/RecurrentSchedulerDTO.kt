package br.com.fitnesspro.shared.communication.dtos.scheduler

import br.com.fitnesspro.shared.communication.dtos.workout.WorkoutDTO
import br.com.fitnesspro.shared.communication.dtos.workout.WorkoutGroupDTO
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull

@Schema(description = "Classe DTO usada para criação de agendamento recorrente")
data class RecurrentSchedulerDTO(
    @Schema(description = "Lista de agendamentos", required = true)
    @field:NotNull(message = "{recurrentSchedulerDTO.schedules.notNull}")
    @field:NotEmpty(message = "{recurrentSchedulerDTO.schedules.notNull}")
    val schedules: List<SchedulerDTO> = emptyList(),

    @Schema(description = "Treino gerado com os dados do agendamento recorrente")
    @field:NotNull(message = "{recurrentSchedulerDTO.workout.notNull}")
    val workoutDTO: WorkoutDTO? = null,

    @Schema(description = "Lista de grupos musculares do treino gerados com os dados do agendamento recorrente")
    @field:NotNull(message = "{recurrentSchedulerDTO.workoutGroups.notNull}")
    @field:NotEmpty(message = "{recurrentSchedulerDTO.workoutGroups.notNull}")
    val workoutGroups: List<WorkoutGroupDTO> = emptyList()
)