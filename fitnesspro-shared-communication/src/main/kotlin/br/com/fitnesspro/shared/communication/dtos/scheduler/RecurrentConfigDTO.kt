package br.com.fitnesspro.shared.communication.dtos.scheduler

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Future
import jakarta.validation.constraints.FutureOrPresent
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.hibernate.validator.constraints.UniqueElements
import java.time.DayOfWeek
import java.time.LocalDate

data class RecurrentConfigDTO(
    @Schema(description = "Data de início", example = "2023-01-01", required = true)
    @field:NotNull(message = "A data de início é obrigatória")
    @field:FutureOrPresent(message = "Data de início inválida")
    val dateStart: LocalDate,

    @Schema(description = "Data de término", example = "2023-12-31", required = true)
    @field:NotNull(message = "A data de fim é obrigatória")
    @field:Future(message = "Data de fim inválida")
    val dateEnd: LocalDate,

    @Schema(description = "Dias da semana", required = true)
    @field:NotNull(message = "Os dias da semana são obrigatórios")
    @field:Size(min = 1, max = 7, message = "Devem ser informados entre 1 e 7 dias da semana")
    @field:UniqueElements(message = "Os dias da semana não podem ser repetidos")
    val dayWeeks: List<DayOfWeek>
)