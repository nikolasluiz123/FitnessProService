package br.com.fitnesspro.shared.communication.dtos.logs

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "Classe DTO usada para atualização de um pacote de log de execução")
data class UpdatableExecutionLogPackageInfosDTO(
    @Schema(description = "Quantidade de itens inseridos. Utilizado na importação de dados.", required = false)
    var insertedItemsCount: Int? = null,

    @Schema(description = "Quantidade de itens atualizados. Utilizado na importação de dados.", required = false)
    var updatedItemsCount: Int? = null,

    @Schema(description = "Quantidade de itens enviados na exportação.", required = false)
    var allItemsCount: Int? = null,

    @Schema(description = "Momento que o client começou a executar a operação.", required = false)
    var clientExecutionStart: LocalDateTime? = null,

    @Schema(description = "Momento que o client terminou de executar a operação.", required = false)
    var clientExecutionEnd: LocalDateTime? = null,

    @Schema(description = "Stack de erro.", required = false)
    var error: String? = null
)
