package br.com.fitnesspro.shared.communication.dtos.scheduler

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Future
import jakarta.validation.constraints.FutureOrPresent
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.hibernate.validator.constraints.UniqueElements
import java.time.DayOfWeek
import java.time.LocalDate

@Schema(description = "Classe DTO usada internamente no SchedulerDTO para informar algumas configurações de um agendamento recorrente")
data class RecurrentConfigDTO(
    @Schema(description = "Data de início", example = "2023-01-01", required = true)
    @field:NotNull(message = "{recurrentConfigDTO.dateStart.notNull}")
    @field:FutureOrPresent(message = "{recurrentConfigDTO.dateStart.futureOrPresent}")
    val dateStart: LocalDate,

    @Schema(description = "Data de término", example = "2023-12-31", required = true)
    @field:NotNull(message = "{recurrentConfigDTO.dateEnd.notNull}")
    @field:Future(message = "{recurrentConfigDTO.dateEnd.future}")
    val dateEnd: LocalDate,

    @Schema(description = "Dias da semana", required = true)
    @field:NotNull(message = "{recurrentConfigDTO.dayWeeks.notNull}")
    @field:Size(min = 1, max = 7, message = "{recurrentConfigDTO.dayWeeks.size}")
    @field:UniqueElements(message = "{recurrentConfigDTO.dayWeeks.uniqueElements}")
    val dayWeeks: List<DayOfWeek>
)