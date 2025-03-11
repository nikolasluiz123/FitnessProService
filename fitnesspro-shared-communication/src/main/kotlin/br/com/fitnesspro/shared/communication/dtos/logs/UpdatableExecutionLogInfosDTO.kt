package br.com.fitnesspro.shared.communication.dtos.logs

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class UpdatableExecutionLogInfosDTO(
    @Schema(description = "Data da última atualização. Utilizado na importação de dados.", required = false)
    var lastUpdateDate: LocalDateTime? = null,

    @Schema(description = "Tamanho da página da execução. Utilizado na exportação e importação de dados.", required = false)
    var pageSize: Int? = null,
)
