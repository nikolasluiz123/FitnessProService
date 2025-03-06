package br.com.fitnesspro.shared.communication.dtos.logs

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class UpdatableExecutionLogInfosDTO(
    @Schema(description = "Momento que o client começou a executar a operação. Nem toda execução vai registrar isso.", required = false, readOnly = true)
    var clientExecutionStart: LocalDateTime? = null,

    @Schema(description = "Momento que o client terminou de executar a operação. Nem toda execução vai registrar isso.", required = false, readOnly = true)
    var clientExecutionEnd: LocalDateTime? = null,
)
