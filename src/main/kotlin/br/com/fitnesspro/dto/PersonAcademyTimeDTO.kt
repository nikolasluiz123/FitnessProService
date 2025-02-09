package br.com.fitnesspro.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.DayOfWeek
import java.time.LocalTime

data class PersonAcademyTimeDTO(
    @Schema(description = "Identificador", example = "e874d31c-0e29-4e9b-b48e-7d70d91b6a16", required = false)
    @field:Size(min = 1, max = 255, message = "O identificador deve ter entre 1 e 255 caracteres")
    val id: String? = null,

    @Schema(description = "Identificador da pessoa", example = "e874d31c-0e29-4e9b-b48e-7d70d91b6a16", required = true)
    @field:NotNull(message = "O identificador da pessoa é obrigatório")
    val personId: String? = null,

    @Schema(description = "Identificador da academia", example = "e874d31c-0e29-4e9b-b48e-7d70d91b6a16", required = true)
    @field:NotNull(message = "O identificador da academia é obrigatório")
    val academyId: String? = null,

    @Schema(description = "Hora de início", example = "08:00", required = true)
    @field:NotNull(message = "A hora de início é obrigatória")
    val timeStart: LocalTime? = null,

    @Schema(description = "Hora de término", example = "10:00", required = true)
    @field:NotNull(message = "A hora de término é obrigatória")
    val timeEnd: LocalTime? = null,

    @Schema(description = "Dia da semana", required = true)
    @field:NotNull(message = "O dia da semana é obrigatório")
    val dayOfWeek: DayOfWeek? = null,

    @Schema(description = "Valor booleano que representa se o registro está ativo", required = true)
    @field:NotNull(message = "O campo ativo é obrigatório")
    val active: Boolean = true
)